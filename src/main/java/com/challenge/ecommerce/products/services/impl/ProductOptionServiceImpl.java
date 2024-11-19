package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductOptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.ProductOptionEntity;
import com.challenge.ecommerce.products.models.VariantEntity;
import com.challenge.ecommerce.products.repositories.OptionRepository;
import com.challenge.ecommerce.products.repositories.ProductOptionRepository;
import com.challenge.ecommerce.products.services.IProductOptionService;
import com.challenge.ecommerce.products.services.IProductOptionValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductOptionServiceImpl implements IProductOptionService {
  OptionRepository optionRepository;
  ProductOptionRepository productOptionRepository;
  IProductOptionValueService productOptionValueService;

  @Override
  public void updateProductOptionAndOptionValues(
      ProductUpdateDto request, ProductEntity product, VariantEntity variant) {
    // Get the List Option and OptionValue of the product
    List<ProductOptionEntity> currentOptions =
        productOptionRepository.findByProductIDAndDeletedAtIsNull(product.getId());

    for (ProductOptionCreateDto optionDto : request.getOptions()) {
      ProductOptionEntity productOption =
          currentOptions.stream()
              .filter(opt -> opt.getOption().getId().equals(optionDto.getIdOption()))
              .findFirst()
              .orElse(null);

      // If the option does not exist, create a new one
      if (productOption == null) {
        var newOption =
            optionRepository
                .findByIdAndDeletedAtIsNull(optionDto.getIdOption())
                .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));
        productOption = new ProductOptionEntity();
        productOption.setOption(newOption);
        productOption.setProduct(product);
        productOptionRepository.save(productOption);
      }

      // Update OptionValue
      productOptionValueService.updateOptionValues(optionDto, productOption, variant);
    }
  }
}
