package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.OptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionUpdateDto;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface IOptionService {
  OptionResponse addOption(OptionCreateDto request);

  ApiResponse<?> getListOptions(Pageable pageable);

  OptionResponse updateOption(OptionUpdateDto request, String id);

  OptionResponse getOptionById(String optionId);

  void deleteOption(String optionId);
}
