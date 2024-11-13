package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.VariantEntity;

public interface IVariantService {
  VariantEntity addProductVariant(ProductUpdateDto request, ProductEntity product);
}
