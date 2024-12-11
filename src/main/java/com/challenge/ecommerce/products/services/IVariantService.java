package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;

public interface IVariantService {
  void updateProductVariant(ProductUpdateDto request, ProductEntity product);

  void deleteByProduct(ProductEntity product);

  void addProductVariant(ProductCreateDto request, ProductEntity product);
}
