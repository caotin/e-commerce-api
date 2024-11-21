package com.challenge.ecommerce.products.controllers;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.services.IProductService;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.StringHelper;
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
  public ResponseEntity<?> addProduct(@RequestBody @Valid ProductCreateDto request) {
    var product = productService.addProduct(request);
    var resp =
        ApiResponse.builder().result(product).message("Create new product successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping
  public ResponseEntity<?> getAllProducts(
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
    Pageable pageable = PageRequest.of(page-1, size, sort);
    var listProducts = productService.getListProducts(pageable, category, minPrice, maxPrice);
    return ResponseEntity.ok(listProducts);
  }

  @GetMapping("/{productSlug}")
  public ResponseEntity<?> getProductBySlug(@PathVariable String productSlug) {
    String formattedSlug = StringHelper.toSlug(productSlug);
    var product = productService.getProductBySlug(formattedSlug);
    var resp = ApiResponse.builder().result(product).message("Get Product Successfully").build();
    return ResponseEntity.ok(resp);
  }

  @PutMapping("/{productSlug}")
  public ResponseEntity<?> updateProductBySlug(
      @PathVariable String productSlug, @RequestBody @Valid ProductUpdateDto request) {
    String formattedSlug = StringHelper.toSlug(productSlug);
    var product = productService.updateProductBySlug(request, formattedSlug);
    var resp = ApiResponse.builder().result(product).message("Update Product Successfully").build();
    return ResponseEntity.ok(resp);
  }

  @DeleteMapping("/{productSlug}")
  public ResponseEntity<?> deleteProductBySlug(@PathVariable String productSlug) {
    String formattedSlug = StringHelper.toSlug(productSlug);
    productService.deleteProductBySlug(formattedSlug);
    var resp = ApiResponse.builder().message("Delete Product Successfully").build();
    return ResponseEntity.ok(resp);
  }
}
