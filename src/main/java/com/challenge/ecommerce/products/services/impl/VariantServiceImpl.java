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

import java.time.LocalDateTime;
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
    if (isSkuIdTakenByAnotherProduct(request.getSku_id(), product.getId())) {
      throw new CustomRuntimeException(ErrorCode.SKU_ID_EXISTED);
    }

    // check variant or sku not exist
    if (product.getVariants().isEmpty()
        || !variantRepository.existsBySkuIdAndProductIdAndDeletedAtIsNull(
            request.getSku_id(), product.getId())) {
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

    var oldVariant =
        variantRepository
            .findBySkuIdAndDeletedAtIsNull(request.getSku_id())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.VARIANT_NOT_FOUND));
    return mapper.updateVariantFromDto(request, oldVariant);
  }

  @Override
  public void deleteByProduct(ProductEntity product) {
    var variant = variantRepository.findByProductIdAndDeletedAtIsNull(product.getId());
    if (variant.isPresent()) {

      variant.get().setDeletedAt(LocalDateTime.now());
      variantRepository.save(variant.get());
    }
  }

  private boolean isSkuIdTakenByAnotherProduct(String skuId, String productId) {
    return variantRepository.existsBySkuIdAndDeletedAtIsNull(skuId)
        && !variantRepository.existsBySkuIdAndProductIdAndDeletedAtIsNull(skuId, productId);
  }
}
