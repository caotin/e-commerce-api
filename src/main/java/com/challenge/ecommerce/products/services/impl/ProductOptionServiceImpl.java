package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.models.ProductOptionEntity;
import com.challenge.ecommerce.products.repositories.OptionRepository;
import com.challenge.ecommerce.products.repositories.ProductOptionRepository;
import com.challenge.ecommerce.products.services.IProductOptionService;
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

  @Override
  public void addProductOption(ProductUpdateDto request, ProductEntity product) {
    List<ProductOptionEntity> options =
        request.getOptions().stream()
            .map(
                child -> {
                  var option =
                      optionRepository
                          .findByIdAndDeletedAtIsNull(child.getIdOption())
                          .orElseThrow(
                              () -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));
                  ProductOptionEntity entity = new ProductOptionEntity();
                  entity.setOption(option);
                  entity.setProduct(product);
                  return entity;
                })
            .toList();
    productOptionRepository.saveAll(options);
  }
}
