package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.*;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.VariantEntity;

public interface IProductOptionService {
  void createProductOptionAndOptionValues(
      VariantCreateDto request, ProductEntity product, VariantEntity variant);

  void updateProductOptionAndOptionValues(
      VariantUpdateDto variantUpdateDto, ProductEntity product, VariantEntity newVariant);
}
