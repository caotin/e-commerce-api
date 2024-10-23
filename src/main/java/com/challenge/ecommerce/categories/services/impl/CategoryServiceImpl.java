package com.challenge.ecommerce.categories.services.impl;

import com.challenge.ecommerce.categories.controllers.dto.CategoryCreateDto;
import com.challenge.ecommerce.categories.controllers.dto.CategoryResponse;
import com.challenge.ecommerce.categories.controllers.dto.CategoryUpdateDto;
import com.challenge.ecommerce.categories.mappers.ICategoryMapper;
import com.challenge.ecommerce.categories.repositories.CategoryRepository;
import com.challenge.ecommerce.categories.services.ICategoryService;
import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.CloudUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

  CategoryRepository categoryRepository;
  final ICategoryMapper mapper;
  CloudUtils cloudinary;

  @Override
  public CategoryResponse addCategory(CategoryCreateDto request) {
    if (categoryRepository.existsByName(request.getName())) {
      throw new CustomRuntimeException(ErrorCode.CATEGORY_EXISTED);
    }
    // mapper dto to entity
    var category = mapper.categoryCreateDtoToEntity(request);
    categoryRepository.save(category);
    // mapper entity to dto
    return mapper.categoryEntityToDto(category);
  }

  @Override
  public ApiResponse<?> getListCategories(Pageable pageable) {
    var categories = categoryRepository.findAllByDeletedAtIsNull(pageable);
    List<CategoryResponse> categoryResponses =
        categories.stream().map(mapper::categoryEntityToDto).toList();
    return ApiResponse.builder()
        .totalPages(categories.getTotalPages())
        .result(categoryResponses)
        .total(categories.getTotalElements())
        .page(pageable.getPageNumber())
        .limit(categories.getNumberOfElements())
        .message("Get list category successfully")
        .build();
  }

  @Override
  public CategoryResponse getCategoryById(String id) {
    var category =
        categoryRepository
            .findByIdAndDeletedAt(id)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    return mapper.categoryEntityToDto(category);
  }

  @Override
  public CategoryResponse updateCategory(CategoryUpdateDto request, String id) {
    var oldCategory =
        categoryRepository
            .findByIdAndDeletedAt(id)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    var newCategory = mapper.updateCategoryFromDto(request, oldCategory);
    categoryRepository.save(newCategory);
    return mapper.categoryEntityToDto(newCategory);
  }

  @Override
  public CategoryResponse createOrUpdateCategoryImage(String id, MultipartFile file) {
    var category =
        categoryRepository
            .findByIdAndDeletedAt(id)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    if (file != null && !file.isEmpty()) {
      if (category.getCategory_img() != null && !category.getCategory_img().isEmpty())
        cloudinary.deleteFileAsync(category.getCategory_img());
      String link;
      try {
        link = cloudinary.uploadFileAsync(file).get();
      } catch (InterruptedException | ExecutionException e) {
        throw new CustomRuntimeException(ErrorCode.SET_IMAGE_NOT_SUCCESS);
      }
      category.setCategory_img(link);
    }
    categoryRepository.save(category);
    return mapper.categoryEntityToDto(category);
  }

  @Override
  public void deleteCategoryImageById(String id) {
    var category =
        categoryRepository
            .findByIdAndDeletedAt(id)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    var file = category.getCategory_img();
    if (file != null) {
      cloudinary.deleteFileAsync(file);
      categoryRepository.deleteImage(file);
    } else throw new CustomRuntimeException(ErrorCode.CATEGORY_IMAGE_NOT_FOUND);
  }

  @Override
  public void deleteCategory(String id) {
    var category =
        categoryRepository
            .findByIdAndDeletedAt(id)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.CATEGORY_NOT_FOUND));
    cloudinary.deleteFileAsync(category.getCategory_img());
    category.setDeletedAt(LocalDateTime.now());
    categoryRepository.save(category);
  }

  @Override
  public ApiResponse<?> getListCategoriesByParentName(
      Pageable pageable, String categoryParentName) {
    var categories = categoryRepository.findByParentName(categoryParentName, pageable);
    List<CategoryResponse> categoryResponses =
        categories.stream().map(mapper::categoryEntityToDto).toList();
    return ApiResponse.builder()
        .totalPages(categories.getTotalPages())
        .result(categoryResponses)
        .total(categories.getTotalElements())
        .page(pageable.getPageNumber())
        .limit(categories.getNumberOfElements())
        .message("Get list category by parent name successfully")
        .build();
  }
}
