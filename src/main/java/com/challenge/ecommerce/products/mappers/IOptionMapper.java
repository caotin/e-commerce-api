package com.challenge.ecommerce.products.mappers;

import com.challenge.ecommerce.products.controllers.dto.OptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionResponse;
import com.challenge.ecommerce.products.controllers.dto.OptionUpdateDto;
import com.challenge.ecommerce.products.models.OptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IOptionMapper {
  OptionEntity optionCreateDtoToEntity(OptionCreateDto request);

  OptionResponse optionEntityToDto(OptionEntity entity);

  OptionEntity updateOptionFromDto(OptionUpdateDto request, @MappingTarget OptionEntity oldEntity);
}
