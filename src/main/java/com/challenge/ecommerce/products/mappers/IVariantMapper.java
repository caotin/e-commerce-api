package com.challenge.ecommerce.products.mappers;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.VariantShortResponse;
import com.challenge.ecommerce.products.models.VariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IVariantMapper {
  VariantEntity variantShortDtoToEntity(VariantShortResponse request);

  VariantEntity productCreateDtoToVariantEntity(ProductCreateDto request);

  @Mapping(target = "createdAt", source = "createdAt")
  VariantShortResponse variantEntityToShortDto(VariantEntity entity);
}
