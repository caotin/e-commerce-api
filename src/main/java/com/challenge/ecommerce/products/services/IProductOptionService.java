package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;

public interface IProductOptionService {
  void addProductOption(ProductUpdateDto request, ProductEntity product);
}
