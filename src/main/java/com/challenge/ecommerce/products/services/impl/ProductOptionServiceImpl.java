package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.*;
import com.challenge.ecommerce.products.models.*;
import com.challenge.ecommerce.products.repositories.OptionRepository;
import com.challenge.ecommerce.products.repositories.OptionValueRepository;
import com.challenge.ecommerce.products.repositories.ProductOptionRepository;
import com.challenge.ecommerce.products.repositories.VariantValueRepository;
import com.challenge.ecommerce.products.services.IProductOptionService;
import com.challenge.ecommerce.products.services.IProductOptionValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductOptionServiceImpl implements IProductOptionService {
  OptionRepository optionRepository;
  ProductOptionRepository productOptionRepository;
  IProductOptionValueService productOptionValueService;
  OptionValueRepository optionValueRepository;
  VariantValueRepository variantValueRepository;

  @Override
  public void createProductOptionAndOptionValues(
      VariantCreateDto request, ProductEntity product, VariantEntity variant) {
    for (ProductOptionCreateDto productOption : request.getOptions()) {
      var optionValue =
          optionValueRepository
              .findByValueAndDeletedAtIsNull(productOption.getValueName())
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_VALUE_NOT_FOUND));
      var optionEntity =
          optionRepository
              .findByIdAndDeletedAtIsNull(optionValue.getOption().getId())
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));

      ProductOptionEntity productOptionEntity = new ProductOptionEntity();
      productOptionEntity.setProduct(product);
      productOptionEntity.setOption(optionEntity);
      productOptionRepository.save(productOptionEntity);

      productOptionValueService.createOptionValues(productOption, productOptionEntity, variant);
    }
  }

  @Override
  public void updateProductOptionAndOptionValues(
      VariantUpdateDto request, ProductEntity product, VariantEntity variant) {
    // Get the List Option and OptionValue of the product
    List<ProductOptionEntity> currentOptions =
        productOptionRepository.findByProductIDAndDeletedAtIsNull(product.getId());
    List<VariantValueEntity> variantValues =
        variantValueRepository.findByVariantIdAndDeletedAtIsNull(request.getVariantId());
    Set<String> requestOptionIds = new HashSet<>();

    log.info("option size {}", request.getOptions().size());

    for (ProductOptionCreateDto optionDto : request.getOptions()) {
      var optionValue =
          optionValueRepository
              .findByValueAndDeletedAtIsNull(optionDto.getValueName())
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_VALUE_NOT_FOUND));

      ProductOptionEntity productOption =
          currentOptions.stream()
              .filter(opt -> opt.getOption().getId().equals(optionValue.getOption().getId()))
              .findFirst()
              .orElse(null);

      // If the option does not exist, create a new one
      if (productOption == null) {
        var newOption =
            optionRepository
                .findByIdAndDeletedAtIsNull(optionValue.getOption().getId())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));
        productOption = new ProductOptionEntity();
        productOption.setOption(newOption);
        productOption.setProduct(product);
        productOptionRepository.save(productOption);
        requestOptionIds.add(newOption.getId());
      } else requestOptionIds.add(optionValue.getOption().getId());
      // Update OptionValue
      productOptionValueService.updateOptionValues(optionDto, productOption, variant);
    }

    // Remove variant value
    List<VariantValueEntity> variantValueTRemove =
        variantValues.stream()
            .filter(variantValue -> !requestOptionIds.contains(variantValue.getOption().getId()))
            .toList();
    for (VariantValueEntity variantValueEntity : variantValueTRemove) {
      variantValueEntity.setDeletedAt(LocalDateTime.now());
      variantValueRepository.save(variantValueEntity);
    }
  }
}
