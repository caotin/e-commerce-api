package com.challenge.ecommerce.products.mappers;

import com.challenge.ecommerce.products.controllers.dto.*;
import com.challenge.ecommerce.products.models.VariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IVariantMapper {

  VariantEntity productDtoToVariantEntity(VariantCreateDto request);

  @Mapping(target = "createdAt", source = "createdAt")
  VariantShortResponse variantEntityToShortDto(VariantEntity entity);

  VariantEntity updateVariantFromDto(
          VariantUpdateDto request, @MappingTarget VariantEntity oldEntity);

  VariantEntity updateDtoToVariantEntity(VariantUpdateDto request);
}
