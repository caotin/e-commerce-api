package com.challenge.ecommerce.products.mappers;

import com.challenge.ecommerce.products.controllers.dto.OptionValueResponse;
import com.challenge.ecommerce.products.models.OptionValueEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IOptionValueMapper {
    OptionValueResponse optionValueEntityToDto(OptionValueEntity entity);
}
