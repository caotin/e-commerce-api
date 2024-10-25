package com.challenge.ecommerce.categories.controllers;

import com.challenge.ecommerce.categories.controllers.dto.CategoryCreateDto;
import com.challenge.ecommerce.categories.controllers.dto.CategoryUpdateDto;
import com.challenge.ecommerce.categories.services.ICategoryService;
import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.StringHelper;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {

  ICategoryService categoryService;

  static final String DEFAULT_FILTER_PAGE = "0";
  static final String DEFAULT_FILTER_SIZE = "10";
  static final Sort DEFAULT_FILTER_SORT = Sort.by(Sort.Direction.DESC, "createdAt");
  static final Sort DEFAULT_FILTER_SORT_ASC = Sort.by(Sort.Direction.ASC, "createdAt");

  @PostMapping
  public ResponseEntity<?> addCategory(@RequestBody @Valid CategoryCreateDto request) {
    var category = categoryService.addCategory(request);
    var resp =
        ApiResponse.builder().result(category).message("Create new category successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping
  public ResponseEntity<?> getAllCategories(
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) int size,
      @RequestParam(required = false) String sortParam) {
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page, size, sort);
    var listCategories = categoryService.getListCategories(pageable);
    return ResponseEntity.ok(listCategories);
  }

  @GetMapping(value = {"/{categorySlug}"})
  public ResponseEntity<?> getCategoryBySlug(@PathVariable("categorySlug") String categorySlug) {
    String formattedSlug = StringHelper.toSlug(categorySlug);
    var category = categoryService.getCategoryBySlug(formattedSlug);
    var resp = ApiResponse.builder().result(category).message("Get category successfully").build();
    return ResponseEntity.ok(resp);
  }

  @PutMapping("/{categorySlug}")
  public ResponseEntity<?> updateCategory(
      @PathVariable("categorySlug") String categorySlug,
      @RequestBody @Valid CategoryUpdateDto request) {
    String formattedSlug = StringHelper.toSlug(categorySlug);
    var category = categoryService.updateCategory(request, formattedSlug);
    var resp =
        ApiResponse.builder().result(category).message("Update category Successfully").build();
    return ResponseEntity.ok(resp);
  }

  @DeleteMapping("/{categorySlug}")
  public ResponseEntity<?> deleteCategory(@PathVariable("categorySlug") String categorySlug) {
    String formattedSlug = StringHelper.toSlug(categorySlug);
    categoryService.deleteCategory(formattedSlug);
    var resp = ApiResponse.builder().message("Category deleted successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/by-parent/{categoryParentSlug}")
  public ResponseEntity<?> getCategoryByParentName(
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) int size,
      @RequestParam(required = false) String sortParam,
      @PathVariable("categoryParentSlug") String categoryParentSlug) {
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page, size, sort);
    String formattedSlug = StringHelper.toSlug(categoryParentSlug);
    var listCategories = categoryService.getListCategoriesByParentSlug(pageable, formattedSlug);
    return ResponseEntity.ok(listCategories);
  }
}
