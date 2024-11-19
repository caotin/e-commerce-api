package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductOptionCreateDto;
import com.challenge.ecommerce.products.models.ProductOptionEntity;
import com.challenge.ecommerce.products.models.VariantEntity;

public interface IProductOptionValueService {
  void updateOptionValues(
      ProductOptionCreateDto request, ProductOptionEntity product, VariantEntity variant);
}
