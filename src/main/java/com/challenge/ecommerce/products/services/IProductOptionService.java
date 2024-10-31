package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.models.ProductEntity;

public interface IProductOptionService {
  void addProductOption(ProductCreateDto request, ProductEntity product);
}
