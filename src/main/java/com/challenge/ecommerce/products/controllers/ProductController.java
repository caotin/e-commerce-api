package com.challenge.ecommerce.products.controllers;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.services.IProductService;
import com.challenge.ecommerce.utils.ApiResponse;
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

  static final String DEFAULT_FILTER_PAGE = "0";
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
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) @Min(0) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) @Min(0) int size,
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
    Pageable pageable = PageRequest.of(page, size, sort);
    var listProducts = productService.getListProducts(pageable, category, minPrice, maxPrice);
    return ResponseEntity.ok(listProducts);
  }
}
