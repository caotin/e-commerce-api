package com.challenge.ecommerce.products.mappers;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.controllers.dto.VariantShortResponse;
import com.challenge.ecommerce.products.models.VariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IVariantMapper {

  VariantEntity productCreateDtoToVariantEntity(ProductCreateDto request);

  @Mapping(target = "createdAt", source = "createdAt")
  VariantShortResponse variantEntityToShortDto(VariantEntity entity);

  VariantEntity updateVariantFromDto(
      ProductUpdateDto request, @MappingTarget VariantEntity oldEntity);
}
