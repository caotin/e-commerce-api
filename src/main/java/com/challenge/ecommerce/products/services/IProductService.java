package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductResponse;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IProductService {
  ProductResponse addProduct(ProductCreateDto request);

  ApiResponse<List<ProductResponse>> getListProducts(Pageable pageable, String category, Integer min, Integer max);

  ProductResponse getProductBySlug(String productSlug);

  ProductResponse updateProductBySlug(ProductUpdateDto request, String productSlug);

  void deleteProductBySlug(String productSlug);

  Integer getTotalStock(String categoryId);

  Map<String, Integer> getBatchTotalStock(List<String> categoryIds);
}
