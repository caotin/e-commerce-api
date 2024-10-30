package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.OptionResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionValueCreateDto;
import com.challenge.ecommerce.products.mappers.IOptionMapper;
import com.challenge.ecommerce.products.models.OptionValueEntity;
import com.challenge.ecommerce.products.repositories.OptionRepository;
import com.challenge.ecommerce.products.repositories.OptionValueRepository;
import com.challenge.ecommerce.products.services.IOptionValueService;
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
public class OptionValueServiceImpl implements IOptionValueService {
  OptionValueRepository optionValueRepository;
  OptionRepository optionRepository;
  IOptionMapper mapper;

  @Override
  public OptionResponse addOptionValue(String optionId, OptionValueCreateDto request) {
    var option =
        optionRepository
            .findByIdAndDeletedAt(optionId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));

    var existingNames =
        optionValueRepository.findAllValueNamesByOptionAndDeletedAtIsNull(option.getId());
    List<OptionValueEntity> valueEntities =
        request.getOptionValues().stream()
            .map(
                child -> {
                  if (existingNames.contains(child.getValueName())) {
                    throw new CustomRuntimeException(ErrorCode.OPTION_VALUE_NAME_EXISTED);
                  }
                  OptionValueEntity valueEntity = new OptionValueEntity();
                  valueEntity.setValue_name(child.getValueName());
                  valueEntity.setOption(option);
                  return valueEntity;
                })
            .toList();
    optionValueRepository.saveAll(valueEntities);
    return mapper.optionEntityToDto(option);
  }
}
