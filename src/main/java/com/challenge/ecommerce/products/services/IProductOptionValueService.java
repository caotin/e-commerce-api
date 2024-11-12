package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductOptionCreateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.ProductOptionEntity;
import com.challenge.ecommerce.products.models.VariantEntity;

public interface IProductOptionValueService {
  void addProductOptionValue(
      ProductCreateDto request, ProductEntity product, VariantEntity variant);

  void updateOptionValues(
      ProductOptionCreateDto request, ProductOptionEntity product, VariantEntity variant);
}
