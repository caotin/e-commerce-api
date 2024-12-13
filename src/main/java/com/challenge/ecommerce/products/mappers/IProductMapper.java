package com.challenge.ecommerce.products.mappers;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductFavoriteResponse;
import com.challenge.ecommerce.products.controllers.dto.ProductResponse;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IProductMapper {
  ProductEntity productCreateDtoToEntity(ProductCreateDto request);

  ProductResponse productEntityToDto(ProductEntity entity);

  @Mapping(target = "oldEntity.variants.id", ignore = true)
  ProductEntity updateProductFromDto(
      ProductUpdateDto request, @MappingTarget ProductEntity oldEntity);

  ProductFavoriteResponse productEntityToFavoriteDto(ProductEntity entity);
}
