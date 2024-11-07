package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductResponse;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface IProductService {
  ProductResponse addProduct(ProductCreateDto request);

  ApiResponse<?> getListProducts(Pageable pageable, String category, Integer min, Integer max);

  ProductResponse getProductBySlug(String productSlug);

  ProductResponse updateProductBySlug(ProductUpdateDto request, String productSlug);

  void deleteProductBySlug(String productSlug);
}
