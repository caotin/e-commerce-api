package com.challenge.ecommerce.products.mappers;

import com.challenge.ecommerce.products.controllers.dto.OptionValueResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionValueUpdateDto;
import com.challenge.ecommerce.products.models.OptionValueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IOptionValueMapper {
  OptionValueResponse optionValueEntityToDto(OptionValueEntity entity);

  OptionValueEntity updateOptionValueFromDto(
      OptionValueUpdateDto request, @MappingTarget OptionValueEntity oldEntity);
}
