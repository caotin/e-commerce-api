package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.mappers.IVariantMapper;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.VariantEntity;
import com.challenge.ecommerce.products.repositories.ProductRepository;
import com.challenge.ecommerce.products.repositories.VariantRepository;
import com.challenge.ecommerce.products.services.IVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VariantServiceImpl implements IVariantService {
  VariantRepository variantRepository;
  IVariantMapper mapper;
  private final ProductRepository productRepository;

  @Override
  public VariantEntity addProductVariant(ProductUpdateDto request, ProductEntity product) {
    if (variantRepository.existsBySkuIdAndDeletedAtIsNull(request.getSku_id())) {
      throw new CustomRuntimeException(ErrorCode.SKU_ID_EXISTED);
    }

    var variant = mapper.productUpdateDtoToVariantEntity(request);
    variant.setProduct(product);

    if (product.getVariants() == null) {
      product.setVariants(new HashSet<>());
    }
    product.getVariants().add(variant);
    variantRepository.save(variant);
    productRepository.save(product);

    return variant;
  }
}
