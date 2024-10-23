package com.challenge.ecommerce.categories.services;

import com.challenge.ecommerce.categories.controllers.dto.CategoryCreateDto;
import com.challenge.ecommerce.categories.controllers.dto.CategoryResponse;
import com.challenge.ecommerce.categories.controllers.dto.CategoryUpdateDto;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ICategoryService {
  CategoryResponse addCategory(CategoryCreateDto request);

  ApiResponse<?> getListCategories(Pageable pageable);

  CategoryResponse getCategoryById(String id);

  CategoryResponse updateCategory(CategoryUpdateDto request, String id);

  CategoryResponse createOrUpdateCategoryImage(String id, MultipartFile file);

  void deleteCategoryImageById(String id);

  void deleteCategory(String id);

  ApiResponse<?> getListCategoriesByParentName(Pageable pageable, String categoryParentName);
}
