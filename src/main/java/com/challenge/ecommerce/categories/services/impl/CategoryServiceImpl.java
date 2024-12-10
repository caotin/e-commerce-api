package com.challenge.ecommerce.categories.services.impl;

import com.challenge.ecommerce.categories.controllers.dto.CategoryCreateDto;
import com.challenge.ecommerce.categories.controllers.dto.CategoryResponse;
import com.challenge.ecommerce.categories.controllers.dto.CategoryUpdateDto;
import com.challenge.ecommerce.categories.mappers.ICategoryMapper;
import com.challenge.ecommerce.categories.models.CategoryEntity;
import com.challenge.ecommerce.categories.repositories.CategoryRepository;
import com.challenge.ecommerce.categories.services.ICategoryService;
import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.services.IProductService;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.StringHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

  CategoryRepository categoryRepository;
  ICategoryMapper mapper;
  IProductService productService;

  @Override
  public CategoryResponse addCategory(CategoryCreateDto request) {
    if (categoryRepository.existsByNameAndDeletedAtIsNull(request.getName())) {
      throw new CustomRuntimeException(ErrorCode.CATEGORY_EXISTED);
    }
    // mapper dto to entity
    var category = mapper.categoryCreateDtoToEntity(request);
    var slug = StringHelper.toSlug(request.getName());
    category.setSlug(slug);
    if (request.getParentCategoryId() != null && !request.getParentCategoryId().isEmpty()) {
      var parentCategory =
          categoryRepository
              .findByIdAndDeletedAt(request.getParentCategoryId())
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_PARENT_NOT_FOUND));
      category.setParentCategory(parentCategory);
    }
    if (request.getImageUrl() != null && !request.getImageUrl().trim().isEmpty()) {
      category.setCategory_img(request.getImageUrl());
    }
    categoryRepository.save(category);
    // mapper entity to dto
    var resp = mapper.categoryEntityToDto(category);
    int productStock = productService.getTotalStock(category.getId());
    resp.setProductStock(productStock);
    return resp;
  }

  @Override
  public ApiResponse<List<CategoryResponse>> getListCategories(Pageable pageable) {
    var categories = categoryRepository.findAllByDeletedAtIsNull(pageable);
    List<CategoryResponse> categoryResponses =
        categories.stream()
            .map(
                category -> {
                  var resp = mapper.categoryEntityToDto(category);
                  setListCategoryParent(resp, category);
                  int productStock = productService.getTotalStock(category.getId());
                  resp.setProductStock(productStock);
                  return resp;
                })
            .toList();
    return ApiResponse.<List<CategoryResponse>>builder()
        .totalPages(categories.getTotalPages())
        .result(categoryResponses)
        .total(categories.getTotalElements())
        .page(pageable.getPageNumber() + 1)
        .limit(categories.getNumberOfElements())
        .message("Get list category successfully")
        .build();
  }

  @Override
  public CategoryResponse getCategoryBySlug(String categorySlug) {
    var category =
        categoryRepository
            .findBySlugAndDeletedAt(categorySlug)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    var resp = mapper.categoryEntityToDto(category);
    if (category.getParentCategory() != null) {
      var parentCategory =
          categoryRepository.findByIdAndDeletedAt(category.getParentCategory().getId());
      resp.setParentCategory(mapper.categoryEntityToParentDto(parentCategory.get()));
    }
    setListCategoryParent(resp, category);
    int productStock = productService.getTotalStock(category.getId());
    resp.setProductStock(productStock);
    return resp;
  }

  @Override
  public CategoryResponse updateCategory(CategoryUpdateDto request, String categorySlug) {
    var oldCategory =
        categoryRepository
            .findBySlugAndDeletedAt(categorySlug)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    if (!request.getName().trim().isEmpty()
        && categoryRepository.existsByNameAndDeletedAtIsNull(request.getName())
        && !request.getName().equals(oldCategory.getName())) {
      throw new CustomRuntimeException(ErrorCode.CATEGORY_EXISTED);
    }
    var newCategory = mapper.updateCategoryFromDto(request, oldCategory);
    if (request.getParentCategoryId() != null && !request.getParentCategoryId().isEmpty()) {
      if (request.getParentCategoryId().equals(oldCategory.getId()))
        throw new CustomRuntimeException(ErrorCode.CATEGORY_PARENT_FAILED_ITSELF);
      var parentCategory =
          categoryRepository
              .findByIdAndDeletedAt(request.getParentCategoryId())
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_PARENT_NOT_FOUND));
      if (parentCategory.getParentCategory() != null
          && parentCategory.getParentCategory().getId().equals(oldCategory.getId())) {
        throw new CustomRuntimeException(ErrorCode.CATEGORY_PARENT_FAILED);
      }
      newCategory.setParentCategory(parentCategory);
    }
    // Thêm xử lý cho hình ảnh mới
    if (request.getImageUrl() != null && !request.getImageUrl().trim().isEmpty()) {
      newCategory.setCategory_img(request.getImageUrl());
    }
    categoryRepository.save(newCategory);
    var resp = mapper.categoryEntityToDto(newCategory);
    setListCategoryParent(resp, newCategory);
    int productStock = productService.getTotalStock(newCategory.getId());
    resp.setProductStock(productStock);
    return resp;
  }

  @Override
  public void deleteCategory(String categorySlug) {
    var category =
        categoryRepository
            .findBySlugAndDeletedAt(categorySlug)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    category.setDeletedAt(LocalDateTime.now());
    for (CategoryEntity child : category.getChildCategories()) {
      child.setParentCategory(null);
      categoryRepository.save(child);
    }
    categoryRepository.save(category);
  }

  @Override
  public ApiResponse<List<CategoryResponse>> getListCategoriesByParentSlug(
      Pageable pageable, String categoryParentSlug) {
    var categories = categoryRepository.findByParentSlugAndDeletedAt(categoryParentSlug, pageable);
    List<CategoryResponse> categoryResponses =
        categories.stream().map(mapper::categoryEntityToDto).toList();
    return ApiResponse.<List<CategoryResponse>>builder()
        .totalPages(categories.getTotalPages())
        .result(categoryResponses)
        .total(categories.getTotalElements())
        .page(pageable.getPageNumber() + 1)
        .limit(categories.getNumberOfElements())
        .message("Get list category by parent name successfully")
        .build();
  }

  void setListCategoryParent(CategoryResponse resp, CategoryEntity newCategory) {
    if (newCategory.getChildCategories() != null) {
      var listCategoryParent = categoryRepository.findByParentIdAndDeletedAt(newCategory.getId());
      var categoryIds = listCategoryParent.stream().map(CategoryEntity::getId).toList();
      var stockMap = productService.getBatchTotalStock(categoryIds);
      List<CategoryResponse> categoryResponses =
          listCategoryParent.stream()
              .map(
                  category -> {
                    var childResp = mapper.categoryEntityToDto(category);
                    childResp.setProductStock(stockMap.getOrDefault(category.getId(), 0));
                    return childResp;
                  })
              .toList();
      resp.setChildCategories(categoryResponses);
    }
  }
}
