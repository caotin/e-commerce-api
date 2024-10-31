package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.VariantEntity;

public interface IProductOptionValueService {
  void addProductOptionValue(
      ProductCreateDto request, ProductEntity product, VariantEntity variant);
}
