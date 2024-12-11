package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.controllers.dto.VariantCreateDto;
import com.challenge.ecommerce.products.controllers.dto.VariantUpdateDto;
import com.challenge.ecommerce.products.mappers.IVariantMapper;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.VariantEntity;
import com.challenge.ecommerce.products.repositories.VariantRepository;
import com.challenge.ecommerce.products.services.IProductOptionService;
import com.challenge.ecommerce.products.services.IProductOptionValueService;
import com.challenge.ecommerce.products.services.IVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VariantServiceImpl implements IVariantService {
  VariantRepository variantRepository;
  IVariantMapper mapper;
  IProductOptionService productOptionService;
  IProductOptionValueService productOptionValueService;

  @Override
  public void updateProductVariant(ProductUpdateDto request, ProductEntity product) {
    // Get current variant
    List<VariantEntity> currentVariants =
        variantRepository.findByProductIdAndDeletedAtIsNull(product.getId());

    // Extract variant IDs from the request
    Set<String> requestVariantIds =
        request.getVariants().stream()
            .map(VariantUpdateDto::getVariantId)
            .filter(Objects::nonNull) // Exclude null values
            .collect(Collectors.toSet());

    // Mark variants for removal (not in request but present in DB)
    currentVariants.stream()
        .filter(variant -> !requestVariantIds.contains(variant.getId()))
        .forEach(
            variant -> {
              variant.setDeletedAt(LocalDateTime.now());
              variantRepository.save(variant);
            });

    for (VariantUpdateDto variantUpdateDto : request.getVariants()) {
      if (isSkuIdTakenByAnotherProduct(variantUpdateDto.getSku_id(), product.getId())) {
        throw new CustomRuntimeException(ErrorCode.SKU_ID_EXISTED);
      }
      var newVariant = new VariantEntity();

      // if request has variantId
      if (variantUpdateDto.getVariantId() != null && !variantUpdateDto.getVariantId().isEmpty()) {
        var oldVariant =
            variantRepository
                .findByIdAndDeletedAtIsNull(variantUpdateDto.getVariantId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.VARIANT_NOT_FOUND));
        newVariant = mapper.updateVariantFromDto(variantUpdateDto, oldVariant);
      } else {
        newVariant = mapper.updateDtoToVariantEntity(variantUpdateDto);
      }
      setVariant(newVariant, product, variantUpdateDto);
    }
  }

  @Override
  public void deleteByProduct(ProductEntity product) {
    var variants = variantRepository.findByProductIdAndDeletedAtIsNull(product.getId());
    if (variants != null) {
      for (var variant : variants) {
        variant.setDeletedAt(LocalDateTime.now());
        variantRepository.save(variant);
      }
    }
  }

  @Override
  public void addProductVariant(ProductCreateDto request, ProductEntity product) {
    for (VariantCreateDto variantCreateDto : request.getVariants()) {
      // check skuId
      if (isSkuIdTakenByAnotherProduct(variantCreateDto.getSku_id(), product.getId())) {
        throw new CustomRuntimeException(ErrorCode.SKU_ID_EXISTED);
      }
      var variant = mapper.productDtoToVariantEntity(variantCreateDto);
      variant.setProduct(product);
      variantRepository.save(variant);

      productOptionService.createProductOptionAndOptionValues(variantCreateDto, product, variant);
      product.getVariants().add(variant);
    }
  }

  private boolean isSkuIdTakenByAnotherProduct(String skuId, String productId) {
    return variantRepository.existsBySkuIdAndDeletedAtIsNull(skuId)
        && !variantRepository.existsBySkuIdAndProductIdAndDeletedAtIsNull(skuId, productId);
  }

  private void setVariant(
      VariantEntity newVariant, ProductEntity product, VariantUpdateDto variantUpdateDto) {
    newVariant.setProduct(product);
    variantRepository.save(newVariant);

    // update productOption
    //    productOptionValueService.updateVariantValue(variantUpdateDto, product, newVariant);

    productOptionService.updateProductOptionAndOptionValues(variantUpdateDto, product, newVariant);
    if (product.getVariants() == null) {
      product.setVariants(new HashSet<>());
    }
    product.getVariants().removeIf(variant -> variant.getId() == null || variant.getId().isEmpty());
    product.getVariants().add(newVariant);
  }
}
