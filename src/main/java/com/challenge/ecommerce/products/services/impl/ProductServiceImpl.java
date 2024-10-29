package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.categories.repositories.CategoryRepository;
import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductImageCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductImageResponse;
import com.challenge.ecommerce.products.controllers.dto.ProductResponse;
import com.challenge.ecommerce.products.mappers.IImageMapper;
import com.challenge.ecommerce.products.mappers.IProductMapper;
import com.challenge.ecommerce.products.models.ImageEntity;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.repositories.ImageRepository;
import com.challenge.ecommerce.products.repositories.ProductRepository;
import com.challenge.ecommerce.products.services.IProductService;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductServiceImpl implements IProductService {

  CategoryRepository categoryRepository;
  IProductMapper mapper;
  ProductRepository productRepository;
  ImageRepository imageRepository;
  IImageMapper imageMapper;

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
    productRepository.save(product);

    // Save list images
    saveImage(request.getImages(), product);

    // set option

    // set option value

    var resp = mapper.productEntityToDto(product);

    // set total fields
    setTotal(resp, product);
    setListImage(resp, product);
    return resp;
  }

  @Override
  public ApiResponse<?> getListProducts(
      Pageable pageable, String category, Integer minPrice, Integer maxPrice) {
    var products =
        productRepository.findAll(
            (root, query, criteriaBuilder) -> {
              var predicates = new ArrayList<>();

              // Điều kiện để kiểm tra nếu deletedAt là null
              predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));

              // Filter by category if provided
              if (category != null && !category.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
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

  void setListImage(ProductResponse resp, ProductEntity product) {
    var listImages = imageRepository.findByIdProductAndDeletedAt(product.getId());
    List<ProductImageResponse> response =
        listImages.stream().map(imageMapper::imageEntityToDto).toList();
    resp.setImages(response);
  }

  void saveImage(List<ProductImageCreateDto> list, ProductEntity product) {
    List<ImageEntity> imageEntities =
        list.stream()
            .map(
                child -> {
                  ImageEntity imageEntity = new ImageEntity();
                  imageEntity.setImages_url(child.getImages_url());
                  imageEntity.setType_image(child.getType_image());
                  imageEntity.setProduct(product);
                  return imageEntity;
                })
            .toList();
    imageRepository.saveAll(imageEntities);
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
}
