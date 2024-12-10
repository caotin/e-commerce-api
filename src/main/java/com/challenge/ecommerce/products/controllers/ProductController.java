package com.challenge.ecommerce.products.controllers;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductResponse;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.services.IProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {

  IProductService productService;

  static final String DEFAULT_FILTER_PAGE = "1";
  static final String DEFAULT_FILTER_SIZE = "10";
  static final Sort DEFAULT_FILTER_SORT = Sort.by(Sort.Direction.DESC, "createdAt");
  static final Sort DEFAULT_FILTER_SORT_ASC = Sort.by(Sort.Direction.ASC, "createdAt");

  @PostMapping
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Create new product successfully",
      content = @Content(schema = @Schema(implementation = ProductResponse.class)))
  @DeleteMapping("/{productSlug}")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "404",
      description = "Category not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(value = "{\n" + "  \"message\": \"Category not found\"\n" + "}")))
  public ResponseEntity<?> addProduct(@RequestBody @Valid ProductCreateDto request) {
    var product = productService.addProduct(request);
    var resp =
        ApiResponse.builder().result(product).message("Create new product successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) @Min(1) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) @Min(1) int size,
      @RequestParam(required = false) String sortParam,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) @Min(0) Integer minPrice,
      @RequestParam(required = false) @Min(0) Integer maxPrice) {
    if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
      throw new CustomRuntimeException(ErrorCode.MIN_PRICE_GREATER_MAX_PRICE);
    }
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page - 1, size, sort);
    var listProducts = productService.getListProducts(pageable, category, minPrice, maxPrice);
    return ResponseEntity.ok().body(listProducts);
  }

  @GetMapping("/{productSlug}")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Get Product Successfully",
      content = @Content(schema = @Schema(implementation = ProductResponse.class)))
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "404",
      description = "Product not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(value = "{\n" + "  \"message\": \"Product not found\"\n" + "}")))
  public ResponseEntity<?> getProductBySlug(@PathVariable String productSlug) {
    String formattedSlug = StringHelper.toSlug(productSlug.trim());
    var product = productService.getProductBySlug(formattedSlug);
    var resp = ApiResponse.builder().result(product).message("Get Product Successfully").build();
    return ResponseEntity.ok(resp);
  }

  @PutMapping("/{productSlug}")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Update Product Successfully",
      content = @Content(schema = @Schema(implementation = ProductResponse.class)))
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "404",
      description = "Product not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(value = "{\n" + "  \"message\": \"Product not found\"\n" + "}")))
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "404",
      description = "Category not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(value = "{\n" + "  \"message\": \"Category not found\"\n" + "}")))
  public ResponseEntity<?> updateProductBySlug(
      @PathVariable String productSlug, @RequestBody @Valid ProductUpdateDto request) {
    String formattedSlug = StringHelper.toSlug(productSlug.trim());
    var product = productService.updateProductBySlug(request, formattedSlug);
    var resp = ApiResponse.builder().result(product).message("Update Product Successfully").build();
    return ResponseEntity.ok(resp);
  }

  @DeleteMapping("/{productSlug}")
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "200",
      description = "Delete product successfully",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(
                      value = "{\n" + "  \"message\": \"Delete product successfully\"\n" + "}")))
  @io.swagger.v3.oas.annotations.responses.ApiResponse(
      responseCode = "404",
      description = "Product not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiResponse.class),
              examples =
                  @ExampleObject(value = "{\n" + "  \"message\": \"Product not found\"\n" + "}")))
  public ResponseEntity<?> deleteProductBySlug(@PathVariable String productSlug) {
    String formattedSlug = StringHelper.toSlug(productSlug.trim());
    productService.deleteProductBySlug(formattedSlug);
    var resp = ApiResponse.builder().message("Delete Product Successfully").build();
    return ResponseEntity.ok(resp);
  }
}
