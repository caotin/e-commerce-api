package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.OptionResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionValueCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionValueResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionValueUpdateDto;
import com.challenge.ecommerce.products.mappers.IOptionMapper;
import com.challenge.ecommerce.products.mappers.IOptionValueMapper;
import com.challenge.ecommerce.products.models.OptionEntity;
import com.challenge.ecommerce.products.models.OptionValueEntity;
import com.challenge.ecommerce.products.repositories.OptionRepository;
import com.challenge.ecommerce.products.repositories.OptionValueRepository;
import com.challenge.ecommerce.products.services.IOptionValueService;
import com.challenge.ecommerce.utils.StringHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OptionValueServiceImpl implements IOptionValueService {
  OptionValueRepository optionValueRepository;
  OptionRepository optionRepository;
  IOptionMapper mapper;
  IOptionValueMapper optionValueMapper;

  @Override
  public OptionResponse addOptionValue(String optionId, OptionValueCreateDto request) {
    var option =
        optionRepository
            .findByIdAndDeletedAtIsNull(optionId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));
    List<OptionValueEntity> valueEntities =
        request.getOptionValues().stream()
            .map(
                child -> {
                  checkExistsOptionValueName(option, child.getValueName());
                  OptionValueEntity valueEntity = new OptionValueEntity();
                  valueEntity.setValue_name(
                      StringHelper.changeFirstCharacterCase(child.getValueName()));
                  valueEntity.setOption(option);
                  return valueEntity;
                })
            .toList();
    optionValueRepository.saveAll(valueEntities);
    var resp = mapper.optionEntityToDto(option);
    setListOptionValue(option, resp);
    return resp;
  }

  @Override
  public OptionResponse updateOptionValue(String optionValueId, OptionValueUpdateDto request) {
    var oldOptionValue =
        optionValueRepository
            .findByIdAndDeletedAtIsNull(optionValueId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_VALUE_NOT_FOUND));
    var option = oldOptionValue.getOption();
    if (option == null || option.getDeletedAt() != null) {
      throw new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND);
    }
    checkExistsOptionValueName(option, request.getValueName());
    var optionValueName =
        request.getValueName().trim().isEmpty()
            ? oldOptionValue.getValue_name()
            : request.getValueName();
    if (optionValueName.trim().isEmpty())
      throw new CustomRuntimeException(ErrorCode.INVALID_OPTION_VALUE_NAME);
    var newOptionValue = optionValueMapper.updateOptionValueFromDto(request, oldOptionValue);
    newOptionValue.setValue_name(StringHelper.changeFirstCharacterCase(optionValueName));
    optionValueRepository.save(newOptionValue);

    var resp = mapper.optionEntityToDto(option);
    setListOptionValue(option, resp);
    return resp;
  }

  @Override
  public OptionValueResponse getOptionValue(String optionValueId) {
    var optionValue =
        optionValueRepository
            .findByIdAndDeletedAtIsNull(optionValueId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_VALUE_NOT_FOUND));
    return optionValueMapper.optionValueEntityToDto(optionValue);
  }

  @Override
  public void setListOptionValue(OptionEntity option, OptionResponse optionResponse) {
    if (option.getOptionValues() != null) {
      List<OptionValueResponse> optionValueResponses =
          option.getOptionValues().stream()
              .map(
                  optionValue -> {
                    if (optionValue.getDeletedAt() != null) {
                      return null;
                    }
                    return optionValueMapper.optionValueEntityToDto(optionValue);
                  })
              .filter(Objects::nonNull)
              .toList();
      optionResponse.setOptionValues(optionValueResponses);
    }
  }

  @Override
  public void deleteOptionValueByOption(OptionEntity option) {
    if (option.getOptionValues() != null) {
      List<OptionValueEntity> optionValueEntities =
          option.getOptionValues().stream()
              .map(
                  optionValue -> {
                    optionValue.setDeletedAt(LocalDateTime.now());
                    return optionValue;
                  })
              .toList();
      optionValueRepository.saveAll(optionValueEntities);
    }
  }

  @Override
  public void deleteOptionValue(String optionValueId) {
    var optionValue =
        optionValueRepository
            .findByIdAndDeletedAtIsNull(optionValueId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_VALUE_NOT_FOUND));
    optionValue.setDeletedAt(LocalDateTime.now());
    optionValueRepository.save(optionValue);
  }

  void checkExistsOptionValueName(OptionEntity option, String optionValueName) {
    var existingNames =
        optionValueRepository.findAllValueNamesByOptionAndDeletedAtIsNull(option.getId());
    if (existingNames.contains(optionValueName)) {
      throw new CustomRuntimeException(ErrorCode.OPTION_VALUE_NAME_EXISTED);
    }
  }
}
