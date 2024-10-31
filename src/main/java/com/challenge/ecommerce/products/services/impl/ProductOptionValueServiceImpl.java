package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductOptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductOptionValueCreateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
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
}
