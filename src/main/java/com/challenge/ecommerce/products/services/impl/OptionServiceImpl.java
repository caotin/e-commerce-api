package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.OptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionUpdateDto;
import com.challenge.ecommerce.products.mappers.IOptionMapper;
import com.challenge.ecommerce.products.repositories.OptionRepository;
import com.challenge.ecommerce.products.services.IOptionService;
import com.challenge.ecommerce.products.services.IOptionValueService;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.StringHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OptionServiceImpl implements IOptionService {
  IOptionMapper mapper;
  OptionRepository optionRepository;
  IOptionValueService optionValueService;

  @Override
  public OptionResponse addOption(OptionCreateDto request) {
    if (optionRepository.existsByOptionNameAndDeletedAtIsNull(request.getOption_name())) {
      throw new CustomRuntimeException(ErrorCode.OPTION_NAME_EXISTED);
    }
    var option = mapper.optionCreateDtoToEntity(request);
    var optionName = StringHelper.changeFirstCharacterCase(request.getOption_name());
    option.setOption_name(optionName);
    optionRepository.save(option);
    var resp = mapper.optionEntityToDto(option);
    optionValueService.setListOptionValue(option, resp);
    return resp;
  }

  @Override
  public ApiResponse<List<OptionResponse>> getListOptions(Pageable pageable) {
    var options = optionRepository.findAllByDeletedAtIsNull(pageable);
    List<OptionResponse> optionResponses =
        options.stream()
            .map(
                optionEntity -> {
                  var resp = mapper.optionEntityToDto(optionEntity);
                  optionValueService.setListOptionValue(optionEntity, resp);
                  return resp;
                })
            .toList();
    return ApiResponse.<List<OptionResponse>>builder()
        .totalPages(options.getTotalPages())
        .result(optionResponses)
        .total(options.getTotalElements())
        .page(pageable.getPageNumber() + 1)
        .limit(options.getNumberOfElements())
        .message("Get List Options Successfully")
        .build();
  }

  @Override
  public OptionResponse updateOption(OptionUpdateDto request, String optionId) {
    var oldOption =
        optionRepository
            .findByIdAndDeletedAtIsNull(optionId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));
    var optionName =
        request.getOption_name().trim().isEmpty()
            ? oldOption.getOption_name()
            : request.getOption_name();
    if (optionName.trim().isEmpty())
      throw new CustomRuntimeException(ErrorCode.OPTION_NAME_CANNOT_BE_NULL);
    if (optionRepository.existsByOptionNameAndDeletedAtIsNull(request.getOption_name())) {
      throw new CustomRuntimeException(ErrorCode.OPTION_NAME_EXISTED);
    }
    var newOption = mapper.updateOptionFromDto(request, oldOption);
    optionName = StringHelper.changeFirstCharacterCase(optionName);
    newOption.setOption_name(optionName);
    optionRepository.save(newOption);
    return getOptionById(optionId);
  }

  @Override
  public OptionResponse getOptionById(String optionId) {
    var option =
        optionRepository
            .findByIdAndDeletedAtIsNull(optionId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));
    var resp = mapper.optionEntityToDto(option);
    optionValueService.setListOptionValue(option, resp);
    return resp;
  }

  @Override
  public void deleteOption(String optionId) {
    var option =
        optionRepository
            .findByIdAndDeletedAtIsNull(optionId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.OPTION_NOT_FOUND));
    option.setDeletedAt(LocalDateTime.now());
    optionValueService.deleteOptionValueByOption(option);
    optionRepository.save(option);
  }
}
