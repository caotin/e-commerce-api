package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.OptionResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionValueCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionValueResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionValueUpdateDto;
import com.challenge.ecommerce.products.models.OptionEntity;

public interface IOptionValueService {
  OptionResponse addOptionValue(String optionId, OptionValueCreateDto request);

  OptionResponse updateOptionValue(String optionId, OptionValueUpdateDto request);

  OptionValueResponse getOptionValue(String optionValueId);

  void setListOptionValue(OptionEntity option, OptionResponse optionResponse);

  void deleteOptionValueByOption(OptionEntity option);

  void deleteOptionValue(String optionValueId);
}
