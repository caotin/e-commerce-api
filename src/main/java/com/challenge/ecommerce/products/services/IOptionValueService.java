package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.OptionResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionValueCreateDto;

public interface IOptionValueService {
    OptionResponse addOptionValue(String optionId, OptionValueCreateDto request);
}
