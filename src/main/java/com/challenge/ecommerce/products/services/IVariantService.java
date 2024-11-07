package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.VariantEntity;

public interface IVariantService {
  VariantEntity addProductVariant(ProductCreateDto request, ProductEntity product);

  VariantEntity updateProductVariant(
      ProductUpdateDto request, VariantEntity variant, ProductEntity product);
}
