package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.OptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionResponse;

public interface IOptionService {
  OptionResponse addOption(OptionCreateDto request);
}
