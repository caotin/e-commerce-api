package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.categories.repositories.CategoryRepository;
import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.favorites.repositories.FavoriteRepository;
import com.challenge.ecommerce.products.controllers.dto.*;
import com.challenge.ecommerce.products.mappers.*;
import com.challenge.ecommerce.products.models.*;
import com.challenge.ecommerce.products.repositories.*;
import com.challenge.ecommerce.products.services.*;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.StringHelper;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductServiceImpl implements IProductService {

  IImageService imageService;
  IVariantService variantService;

  ProductOptionRepository productOptionRepository;
  VariantValueRepository variantValueRepository;
  CategoryRepository categoryRepository;
  ProductRepository productRepository;
  ImageRepository imageRepository;

  IProductMapper mapper;
  IImageMapper imageMapper;
  IVariantMapper variantMapper;
  IOptionMapper optionMapper;
  IOptionValueMapper optionValueMapper;
  private final FavoriteRepository favoriteRepository;

  @Transactional
  @Override
  public ProductResponse addProduct(ProductCreateDto request) {
    var category =
        categoryRepository
            .findByIdAndDeletedAt(request.getCategoryId())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    var product = mapper.productCreateDtoToEntity(request);
    product.setCategory(category);

    var title = StringHelper.changeFirstCharacterCase(request.getTitle().trim().strip());
    log.info("Title: {}", title);
    if (productRepository.existsByTitleAndDeletedAtIsNull(title)) {
      throw new CustomRuntimeException(ErrorCode.PRODUCT_NAME_EXISTED);
    }
    // set slug
    var slug = StringHelper.toSlug(request.getTitle().trim());
    product.setSlug(slug);
    product.setTitle(title);

    log.info("Title product: {}", product.getTitle());
    productRepository.save(product);
    product.setVariants(new HashSet<>());

    // update variants
    variantService.addProductVariant(request, product);

    // update image
    if (request.getImages() != null) {
      imageService.updateImage(request.getImages(), product);
    }

    var resp = mapper.productEntityToDto(product);
    setAdditionalProduct(resp, product);
    return resp;
  }

  @Override
  public ApiResponse<List<ProductResponse>> getListProducts(
      Pageable pageable, String category, Integer minPrice, Integer maxPrice) {
    var products =
        productRepository.findAll(
            (root, query, criteriaBuilder) -> {
              var predicates = new ArrayList<>();

              // Check nullable deletedAt
              predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));

              // Filter by category if provided
              if (category != null && !category.isEmpty()) {
                var categorySearch = StringHelper.toSlug(category);
                var categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("slug"), categorySearch));
              }

              // Filter by minimum price if provided
              if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
              }

              // Filter by maximum price if provided
              if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
              }

              // Combine all predicates with AND
              query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
              return query.getRestriction();
            },
            pageable);
    List<ProductResponse> productResponses =
        products.stream()
            .map(
                product -> {
                  var response = mapper.productEntityToDto(product);
                  setAdditionalProduct(response, product);
                  return response;
                })
            .toList();
    return ApiResponse.<List<ProductResponse>>builder()
        .totalPages(products.getTotalPages())
        .result(productResponses)
        .total(products.getTotalElements())
        .page(pageable.getPageNumber() + 1)
        .limit(products.getNumberOfElements())
        .message("Get list product successfully")
        .build();
  }

  @Override
  public ProductResponse getProductBySlug(String productSlug) {
    var product =
        productRepository
            .findBySlugAndDeletedAtIsNull(productSlug)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));
    var resp = mapper.productEntityToDto(product);
    setAdditionalProduct(resp, product);
    return resp;
  }

  @Transactional
  @Override
  public ProductResponse updateProductBySlug(ProductUpdateDto request, String productSlug) {
    var oldProduct =
        productRepository
            .findBySlugAndDeletedAtIsNull(productSlug)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));
    var newProduct = mapper.updateProductFromDto(request, oldProduct);
    // update category
    if (request.getCategoryId() != null) {
      var category =
          categoryRepository
              .findByIdAndDeletedAt(request.getCategoryId())
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
      newProduct.setCategory(category);
    }

    // update tile and description
    var description =
        request.getDescription() == null ? oldProduct.getDescription() : request.getDescription();
    var title = request.getTitle() == null ? oldProduct.getTitle() : request.getTitle();
    var newTitle = StringHelper.changeFirstCharacterCase(title.trim().strip());

    if (productRepository.existsByTitleAndDeletedAtIsNull(newTitle)
        && !oldProduct.getTitle().trim().equals(newTitle)) {
      throw new CustomRuntimeException(ErrorCode.PRODUCT_NAME_EXISTED);
    }
    newProduct.setTitle(newTitle);
    newProduct.setSlug(StringHelper.toSlug(newTitle.trim()));

    if (description.isBlank()) {
      throw new CustomRuntimeException(ErrorCode.DESCRIPTION_CANNOT_BE_NULL);
    }
    newProduct.setDescription(description);

    // update image
    if (request.getImages() != null) {
      imageService.updateImage(request.getImages(), newProduct);
    }

    // update variants
    variantService.updateProductVariant(request, newProduct);

    productRepository.save(newProduct);

    var resp = mapper.productEntityToDto(newProduct);
    log.info("test 1");
    log.info("product variant size {}", newProduct.getVariants().size());
    setAdditionalProduct(resp, newProduct);
    return resp;
  }

  @Override
  public void deleteProductBySlug(String productSlug) {
    var product =
        productRepository
            .findBySlugAndDeletedAtIsNull(productSlug)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));
    product.setDeletedAt(LocalDateTime.now());
    variantService.deleteByProduct(product);
    productRepository.save(product);
  }

  @Override
  public Integer getTotalStock(String categoryId) {
    var products = productRepository.findByCategoryIdAndDeletedAtIsNull(categoryId);
    int productStock = 0;
    for (var product : products) {
      if (product.getVariants() != null && !product.getVariants().isEmpty()) {
        for (var variant : product.getVariants()) {
          if (variant.getId() != null) {
            productStock += variant.getStock_quantity();
          }
        }
      }
    }
    return productStock;
  }

  @Override
  public Map<String, Integer> getBatchTotalStock(List<String> categoryIds) {
    List<Object[]> results = productRepository.findByCategoryIdsAndDeletedAtIsNull(categoryIds);

    // Covert the result to map
    Map<String, Integer> stockMap = new HashMap<>();
    for (Object[] result : results) {
      String categoryId = (String) result[0]; // categoryId
      Long totalStock = (Long) result[1]; // totalStock
      stockMap.put(categoryId, totalStock.intValue());
    }

    return stockMap;
  }

  void setListImage(ProductResponse resp, ProductEntity product) {
    var listImages = imageRepository.findByIdProductAndDeletedAtIsNull(product.getId());
    List<ProductImageResponse> response =
        listImages.stream().map(imageMapper::imageEntityToDto).toList();
    resp.setImages(response);
  }

  void setTotal(ProductResponse resp, ProductEntity product) {
    // set total favorites
    int totalFavorites = favoriteRepository.findAllFavoriteByProductIdAndDeletedAtIsNull(product.getId());
    resp.setTotalFavorites(totalFavorites);

    // set total rates
    var totalRating = 0;
    resp.setTotalRates(totalRating);

    // set total sold
    var totalSold = 0;
    resp.setTotalSold(totalSold);
  }

  void setListProductOption(VariantShortResponse resp, ProductEntity product) {
    var options = productOptionRepository.findByProductIdAndDeletedAtIsNull(product.getId());
    List<OptionResponse> optionResponses =
        options.stream()
            .map(
                option -> {
                  var optionResp = optionMapper.optionEntityToDto(option);
                  boolean check = setListOptionValue(optionResp, product, resp);
                  return check ? optionResp : null;
                })
            .filter(Objects::nonNull)
            .toList();

    resp.setOptions(optionResponses);
  }

  boolean setListOptionValue(
      OptionResponse resp, ProductEntity product, VariantShortResponse variant) {
    var optionValues =
        variantValueRepository.findByVariantIdAndOptionIdAndDeletedAtIsNull(
            variant.getId(), resp.getId());
    if (!optionValues.isEmpty()) {
      List<OptionValueResponse> optionValueResponses =
          optionValues.stream().map(optionValueMapper::optionValueEntityToDto).toList();
      resp.setOptionValues(optionValueResponses);
      return true;
    }
    return false;
  }

  void setListVariant(ProductResponse resp, ProductEntity product) {
    if (product.getVariants() != null) {
      List<VariantShortResponse> variants =
          product.getVariants().stream()
              .map(
                  variant -> {
                    if (variant.getDeletedAt() == null) {
                      var variantResponse = variantMapper.variantEntityToShortDto(variant);
                      setListProductOption(variantResponse, product);
                      return variantResponse;
                    }
                    return null;
                  })
              .filter(Objects::nonNull)
              .toList();
      resp.setVariants(variants);
    }
  }

  void setAdditionalProduct(ProductResponse resp, ProductEntity product) {
    setTotal(resp, product);
    setListImage(resp, product);

    // set variant
    setListVariant(resp, product);
    // set product stock
    resp.getCategory().setProductStock(getTotalStock(product.getCategory().getId()));
    if (resp.getCategory().getChildCategories() != null) {
      resp.getCategory()
          .getChildCategories()
          .forEach(
              childCategory -> {
                childCategory.setProductStock(getTotalStock(childCategory.getId()));
              });
    }
    // set review
  }
}
