package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductOptionCreateDto;
import com.challenge.ecommerce.products.models.ProductOptionEntity;
import com.challenge.ecommerce.products.models.VariantEntity;
import com.challenge.ecommerce.products.models.VariantValueEntity;
import com.challenge.ecommerce.products.repositories.OptionValueRepository;
import com.challenge.ecommerce.products.repositories.VariantValueRepository;
import com.challenge.ecommerce.products.services.IProductOptionValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductOptionValueServiceImpl implements IProductOptionValueService {
  OptionValueRepository optionValueRepository;
  VariantValueRepository variantValueRepository;

  @Override
  public void updateOptionValues(
      ProductOptionCreateDto optionDto, ProductOptionEntity productOption, VariantEntity variant) {
    List<VariantValueEntity> currentValues =
        variantValueRepository.findByVariantIDAndOptionIdAndDeletedAtIsNull(
            variant.getId(), productOption.getOption().getId());

    // check optionValue existed
    var optionValue =
        optionValueRepository
            .findByIdAndDeletedAtIsNull(optionDto.getIdOptionValue())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_VALUE_NOT_FOUND));
    // check if optionValue is in option
    if (!optionValue.getOption().getId().equals(productOption.getOption().getId())) {
      throw new CustomRuntimeException(ErrorCode.INVALID_OPTION_VALUE_FOR_OPTION);
    }
    VariantValueEntity variantValue =
        currentValues.stream()
            .filter(val -> val.getOptionValue().getId().equals(optionDto.getIdOptionValue()))
            .findFirst()
            .orElse(null);
    // If the optionValue does not exist, create a new one
    if (variantValue == null) {
      VariantValueEntity newVariantValue = new VariantValueEntity();
      newVariantValue.setOption(productOption.getOption());
      newVariantValue.setVariant(variant);
      newVariantValue.setOptionValue(optionValue);
      variantValueRepository.save(newVariantValue);
    }

    // Remove the optionValue that is not included in the request
    List<VariantValueEntity> valuesToRemove =
        currentValues.stream()
            .filter(value -> !optionDto.getIdOptionValue().contains(value.getOptionValue().getId()))
            .toList();
    for (VariantValueEntity valueToRemove : valuesToRemove) {
      valueToRemove.setDeletedAt(LocalDateTime.now());
      variantValueRepository.save(valueToRemove);
    }
  }
}
