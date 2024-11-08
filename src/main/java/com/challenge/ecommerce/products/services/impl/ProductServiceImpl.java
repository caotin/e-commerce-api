package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.categories.repositories.CategoryRepository;
import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductServiceImpl implements IProductService {

  IImageService imageService;
  IVariantService variantService;
  IProductOptionService productOptionService;
  IProductOptionValueService productOptionValueService;

  ProductOptionRepository productOptionRepository;
  VariantValueRepository variantValueRepository;
  VariantRepository variantRepository;
  CategoryRepository categoryRepository;
  ProductRepository productRepository;
  ImageRepository imageRepository;

  IProductMapper mapper;
  IImageMapper imageMapper;
  IVariantMapper variantMapper;
  IOptionMapper optionMapper;
  IOptionValueMapper optionValueMapper;

  @Transactional
  @Override
  public ProductResponse addProduct(ProductCreateDto request) {
    var category =
        categoryRepository
            .findByIdAndDeletedAt(request.getCategoryId())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    var product = mapper.productCreateDtoToEntity(request);
    product.setCategory(category);
    // set slug
    var slug = StringHelper.toSlug(request.getTitle());
    product.setSlug(slug);

    var title = StringHelper.changeFirstCharacterCase(request.getTitle());
    if (productRepository.existsByTitleAndDeletedAtIsNull(title)) {
      throw new CustomRuntimeException(ErrorCode.PRODUCT_NAME_EXISTED);
    }
    product.setTitle(title);
    productRepository.save(product);

    // Save list images
    imageService.saveImage(request.getImages(), product);

    // set variants
    var variant = variantService.addProductVariant(request, product);

    // set option
    productOptionService.addProductOption(request, product);

    // set option value
    productOptionValueService.addProductOptionValue(request, product, variant);

    var resp = mapper.productEntityToDto(product);

    // set total fields
    setTotal(resp, product);
    setListImage(resp, product);

    // set variant
    setListVariant(resp, product);
    // set review
    return resp;
  }

  @Override
  public ApiResponse<?> getListProducts(
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
                  setListImage(response, product);
                  setListVariant(response, product);
                  return response;
                })
            .toList();
    return ApiResponse.builder()
        .totalPages(products.getTotalPages())
        .result(productResponses)
        .total(products.getTotalElements())
        .page(pageable.getPageNumber())
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
    setListVariant(resp, product);
    setListImage(resp, product);
    return resp;
  }

  @Override
  public ProductResponse updateProductBySlug(ProductUpdateDto request, String productSlug) {
    var oldProduct =
        productRepository
            .findBySlugAndDeletedAtIsNull(productSlug)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));
    var oldVariant =
        variantRepository
            .findByProductIDAndDeletedAtIsNull(oldProduct.getId())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.VARIANT_NOT_FOUND));
    var newProduct = mapper.updateProductFromDto(request, oldProduct);
    // update category
    if (request.getCategoryId() != null) {
      var category =
          categoryRepository
              .findByIdAndDeletedAt(request.getCategoryId())
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
      newProduct.setCategory(category);
    }
    var description =
        request.getDescription() == null ? oldProduct.getDescription() : request.getDescription();
    // update tile and description
    var title = StringHelper.changeFirstCharacterCase(request.getTitle());
    if (productRepository.existsByTitleAndDeletedAtIsNull(title)
        && !oldProduct.getTitle().equals(title)) {
      throw new CustomRuntimeException(ErrorCode.PRODUCT_NAME_EXISTED);
    }
    newProduct.setTitle(title);
    newProduct.setSlug(StringHelper.toSlug(title));
    if (description.isBlank()) {
      throw new CustomRuntimeException(ErrorCode.DESCRIPTION_CANNOT_BE_NULL);
    }
    newProduct.setDescription(description);

    // update image
    if (request.getImages() != null) {
      imageService.updateImage(request.getImages(), newProduct);
    }
    // update variants
    var newVariant = variantService.updateProductVariant(request, oldVariant, newProduct);

    if (request.getOptions() != null) {
      // set option
      productOptionService.updateProductOptionAndOptionValues(request, newProduct, newVariant);
    }
    productRepository.save(newProduct);
    var resp = mapper.productEntityToDto(newProduct);
    setTotal(resp, newProduct);
    setListImage(resp, newProduct);

    // set variant
    setListVariant(resp, newProduct);
    // set review
    return resp;
  }

  @Override
  public void deleteProductBySlug(String productSlug) {
    var product =
        productRepository
            .findBySlugAndDeletedAtIsNull(productSlug)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));
    product.setDeletedAt(LocalDateTime.now());
    productRepository.save(product);
  }

  void setListImage(ProductResponse resp, ProductEntity product) {
    var listImages = imageRepository.findByIdProductAndDeletedAtIsNull(product.getId());
    List<ProductImageResponse> response =
        listImages.stream().map(imageMapper::imageEntityToDto).toList();
    resp.setImages(response);
  }

  void setTotal(ProductResponse resp, ProductEntity product) {
    // set total favorites
    var totalFavorites = 0;
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
                  setListOptionValue(optionResp, product, resp);
                  return optionResp;
                })
            .toList();

    resp.setOptions(optionResponses);
  }

  void setListOptionValue(
      OptionResponse resp, ProductEntity product, VariantShortResponse variant) {
    var optionValues =
        variantValueRepository.findByVariantIdAndOptionIdAndDeletedAtIsNull(
            variant.getId(), resp.getId());
    List<OptionValueResponse> optionValueResponses =
        optionValues.stream().map(optionValueMapper::optionValueEntityToDto).toList();
    resp.setOptionValues(optionValueResponses);
  }

  void setListVariant(ProductResponse resp, ProductEntity product) {
    if (product.getVariants() != null) {
      List<VariantShortResponse> variants =
          product.getVariants().stream()
              .map(
                  variant -> {
                    var variantResponse = variantMapper.variantEntityToShortDto(variant);
                    setListProductOption(variantResponse, product);
                    return variantResponse;
                  })
              .toList();
      resp.setVariants(variants);
    }
  }
}
