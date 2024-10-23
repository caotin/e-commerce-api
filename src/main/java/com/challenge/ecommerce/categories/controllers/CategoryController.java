package com.challenge.ecommerce.categories.controllers;

import com.challenge.ecommerce.categories.controllers.dto.CategoryCreateDto;
import com.challenge.ecommerce.categories.controllers.dto.CategoryUpdateDto;
import com.challenge.ecommerce.categories.services.ICategoryService;
import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {

  ICategoryService categoryService;

  static final String DEFAULT_FILTER_PAGE = "0";
  static final String DEFAULT_FILTER_SiZE = "10";
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
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SiZE) int size,
      @RequestParam(required = false) String sortParam) {
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page, size, sort);
    var listCategories = categoryService.getListCategories(pageable);
    return ResponseEntity.ok(listCategories);
  }

  @GetMapping(value = {"/{categoryId}"})
  public ResponseEntity<?> getCategoryById(@PathVariable("categoryId") String categoryId) {
    var category = categoryService.getCategoryById(categoryId);
    var resp = ApiResponse.builder().result(category).message("Get category successfully").build();
    return ResponseEntity.ok(resp);
  }

  @PutMapping("/{categoryId}")
  public ResponseEntity<?> updateCategory(
      @PathVariable String categoryId, @RequestBody(required = false) CategoryUpdateDto request) {
    String name = request.getName();
    if (name == null || name.isEmpty()) {
      throw new CustomRuntimeException(ErrorCode.CATEGORY_NAME_EMPTY);
    }
    var category = categoryService.updateCategory(request, categoryId);
    var resp =
        ApiResponse.builder().result(category).message("Update category Successfully").build();
    return ResponseEntity.ok(resp);
  }

  @PostMapping(
      value = {"/images/{categoryId}"},
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateImageCategory(
      @PathVariable String categoryId, @RequestParam MultipartFile image) {
    if (image == null || image.isEmpty()) {
      throw new CustomRuntimeException(ErrorCode.IMAGE_NOT_FOUND);
    }
    String fileName = image.getOriginalFilename();
    if (!Objects.requireNonNull(fileName).endsWith(".jpg")
        && !fileName.endsWith(".png")
        && !fileName.endsWith(".tiff")
        && !fileName.endsWith(".webp")
        && !fileName.endsWith(".jfif")) {
      throw new CustomRuntimeException(ErrorCode.IMAGE_NOT_SUPPORT);
    } else {
      var category = categoryService.createOrUpdateCategoryImage(categoryId, image);
      var resp =
          ApiResponse.builder()
              .result(category)
              .message("Update image category successfully")
              .build();
      return ResponseEntity.ok(resp);
    }
  }

  @DeleteMapping("/images/{categoryId}")
  public ResponseEntity<?> deleteImageCategory(@PathVariable String categoryId) {
    categoryService.deleteCategoryImageById(categoryId);
    var resp = ApiResponse.builder().message("Category image deleted successfully").build();
    return ResponseEntity.ok(resp);
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<?> deleteCategory(@PathVariable String categoryId) {
    categoryService.deleteCategory(categoryId);
    var resp = ApiResponse.builder().message("Category deleted successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/by-parent/{categoryParentName}")
  public ResponseEntity<?> getCategoryByParentName(
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SiZE) int size,
      @RequestParam(required = false) String sortParam,
      @PathVariable("categoryParentName") String categoryParentName) {
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page, size, sort);
    var listCategories =
        categoryService.getListCategoriesByParentName(pageable, categoryParentName);
    return ResponseEntity.ok(listCategories);
  }
}
