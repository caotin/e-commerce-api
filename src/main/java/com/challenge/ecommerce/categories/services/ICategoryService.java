package com.challenge.ecommerce.categories.services;

import com.challenge.ecommerce.categories.controllers.dto.CategoryCreateDto;
import com.challenge.ecommerce.categories.controllers.dto.CategoryResponse;
import com.challenge.ecommerce.categories.controllers.dto.CategoryUpdateDto;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
  CategoryResponse addCategory(CategoryCreateDto request);

  ApiResponse<List<CategoryResponse>> getListCategories(Pageable pageable);

  CategoryResponse getCategoryBySlug(String categorySlug);

  CategoryResponse updateCategory(CategoryUpdateDto request, String categorySlug);

  void deleteCategory(String id);

  ApiResponse<List<CategoryResponse>> getListCategoriesByParentSlug(
      Pageable pageable, String categoryParentSlug);
}
