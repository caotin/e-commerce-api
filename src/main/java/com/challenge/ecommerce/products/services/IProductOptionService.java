package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.VariantEntity;

public interface IProductOptionService {
  void addProductOption(ProductCreateDto request, ProductEntity product);

  void updateProductOptionAndOptionValues(
      ProductUpdateDto request, ProductEntity product, VariantEntity variant);
}
