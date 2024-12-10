package com.challenge.ecommerce.categories.controllers;

import com.challenge.ecommerce.categories.controllers.dto.CategoryCreateDto;
import com.challenge.ecommerce.categories.controllers.dto.CategoryResponse;
import com.challenge.ecommerce.categories.controllers.dto.CategoryUpdateDto;
import com.challenge.ecommerce.categories.services.ICategoryService;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.StringHelper;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryController {

  ICategoryService categoryService;

  static final String DEFAULT_FILTER_PAGE = "1";
  static final String DEFAULT_FILTER_SIZE = "10";
  static final Sort DEFAULT_FILTER_SORT = Sort.by(Sort.Direction.DESC, "createdAt");
  static final Sort DEFAULT_FILTER_SORT_ASC = Sort.by(Sort.Direction.ASC, "createdAt");

  @PostMapping
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Create new category successfully",
      content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "400",
      description = "Invalid input",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(value = "{\n" + "  \"message\": \"Invalid input\"\n" + "}")))
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "404",
      description = "Category parent not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(
                      value = "{\n" + "  \"message\": \"Category parent not found\"\n" + "}")))
  public ResponseEntity<?> addCategory(@RequestBody @Valid CategoryCreateDto request) {
    var category = categoryService.addCategory(request);
    var resp =
        ApiResponse.builder().result(category).message("Create new category successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) @Min(1) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) @Min(0) int size,
      @RequestParam(required = false) String sortParam) {
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page - 1, size, sort);
    var listCategories = categoryService.getListCategories(pageable);
    return ResponseEntity.ok().body(listCategories);
  }

  @GetMapping(value = {"/{categorySlug}"})
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Get category successfully",
      content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "404",
      description = "Category not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(value = "{\n" + "  \"message\": \"Category not found\"\n" + "}")))
  public ResponseEntity<?> getCategoryBySlug(@PathVariable("categorySlug") String categorySlug) {
    String formattedSlug = StringHelper.toSlug(categorySlug);
    var category = categoryService.getCategoryBySlug(formattedSlug);
    var resp = ApiResponse.builder().result(category).message("Get category successfully").build();
    return ResponseEntity.ok(resp);
  }

  @PutMapping("/{categorySlug}")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Update category Successfully",
      content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "404",
      description = "Category not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(value = "{\n" + "  \"message\": \"Category not found\"\n" + "}")))
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
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Category deleted successfully",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(
                      value = "{\n" + "  \"message\": \"Category deleted successfully\"\n" + "}")))
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "404",
      description = "Category not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(value = "{\n" + "  \"message\": \"Category not found\"\n" + "}")))
  public ResponseEntity<?> deleteCategory(@PathVariable("categorySlug") String categorySlug) {
    String formattedSlug = StringHelper.toSlug(categorySlug);
    categoryService.deleteCategory(formattedSlug);
    var resp = ApiResponse.builder().message("Category deleted successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/by-parent/{categoryParentSlug}")
  public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoryByParentName(
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) @Min(1) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) @Min(0) int size,
      @RequestParam(required = false) String sortParam,
      @PathVariable("categoryParentSlug") String categoryParentSlug) {
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page - 1, size, sort);
    String formattedSlug = StringHelper.toSlug(categoryParentSlug);
    var listCategories = categoryService.getListCategoriesByParentSlug(pageable, formattedSlug);
    return ResponseEntity.ok().body(listCategories);
  }
}
