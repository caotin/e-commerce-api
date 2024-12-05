package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductOptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.VariantUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.ProductOptionEntity;
import com.challenge.ecommerce.products.models.VariantEntity;

public interface IProductOptionValueService {
  void updateOptionValues(
      ProductOptionCreateDto request, ProductOptionEntity product, VariantEntity variant);

  void createOptionValues(
      ProductOptionCreateDto productOptionCreateDto,
      ProductOptionEntity productOption,
      VariantEntity variantEntity);

  void updateVariantValue(
      VariantUpdateDto variantUpdateDto, ProductEntity product, VariantEntity newVariant);
}
