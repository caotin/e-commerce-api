package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.OptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionResponse;
import com.challenge.ecommerce.products.mappers.IOptionMapper;
import com.challenge.ecommerce.products.repositories.OptionRepository;
import com.challenge.ecommerce.products.services.IOptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OptionServiceImpl implements IOptionService {
  IOptionMapper mapper;
  private final OptionRepository optionRepository;

  @Override
  public OptionResponse addOption(OptionCreateDto request) {
    if (optionRepository.existsByOptionNameAndDeletedAtIsNull(request.getOption_name())) {
      throw new CustomRuntimeException(ErrorCode.OPTION_NAME_EXISTED);
    }
    var option = mapper.optionCreateDtoToEntity(request);
    optionRepository.save(option);
    return mapper.optionEntityToDto(option);
  }
}
