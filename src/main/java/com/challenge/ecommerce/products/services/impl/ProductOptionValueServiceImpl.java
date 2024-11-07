package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductOptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductOptionValueCreateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.ProductOptionEntity;
import com.challenge.ecommerce.products.models.VariantEntity;
import com.challenge.ecommerce.products.models.VariantValueEntity;
import com.challenge.ecommerce.products.repositories.OptionRepository;
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
  OptionRepository optionRepository;
  OptionValueRepository optionValueRepository;
  VariantValueRepository variantValueRepository;

  @Override
  public void addProductOptionValue(
      ProductCreateDto request, ProductEntity product, VariantEntity variant) {

    for (ProductOptionCreateDto optionEntity : request.getOptions()) {
      var option =
          optionRepository
              .findByIdAndDeletedAtIsNull(optionEntity.getIdOption())
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));

      for (ProductOptionValueCreateDto valueCreateDto : optionEntity.getOptionValues()) {
        var optionValue =
            optionValueRepository
                .findByIdAndDeletedAtIsNull(valueCreateDto.getIdOptionValue())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_VALUE_NOT_FOUND));
        VariantValueEntity entity = new VariantValueEntity();
        entity.setVariant(variant);
        entity.setOptionValue(optionValue);
        entity.setOption(option);
        variantValueRepository.save(entity);
      }
    }
  }

  @Override
  public void updateOptionValues(
      ProductOptionCreateDto optionDto, ProductOptionEntity productOption, VariantEntity variant) {
    List<VariantValueEntity> currentValues =
        variantValueRepository.findByVariantIDAndOptionIdAndDeletedAtIsNull(
            variant.getId(), productOption.getOption().getId());

    for (ProductOptionValueCreateDto valueDto : optionDto.getOptionValues()) {
      VariantValueEntity variantValue =
          currentValues.stream()
              .filter(val -> val.getOptionValue().getId().equals(valueDto.getIdOptionValue()))
              .findFirst()
              .orElse(null);

      // If the optionValue does not exist, create a new one
      if (variantValue == null) {
        var newOptionValue =
            optionValueRepository
                .findByIdAndDeletedAtIsNull(valueDto.getIdOptionValue())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_VALUE_NOT_FOUND));
        VariantValueEntity newVariantValue = new VariantValueEntity();
        newVariantValue.setOption(productOption.getOption());
        newVariantValue.setVariant(variant);
        newVariantValue.setOptionValue(newOptionValue);
        variantValueRepository.save(newVariantValue);
      }
    }

    // Remove the optionValue that is not included in the request
    List<String> requestValueIds =
        optionDto.getOptionValues().stream()
            .map(ProductOptionValueCreateDto::getIdOptionValue)
            .toList();
    List<VariantValueEntity> valuesToRemove =
        currentValues.stream()
            .filter(value -> !requestValueIds.contains(value.getOptionValue().getId()))
            .toList();
    for (VariantValueEntity valueToRemove : valuesToRemove) {
      valueToRemove.setDeletedAt(LocalDateTime.now());
      variantValueRepository.save(valueToRemove);
    }
  }
}
