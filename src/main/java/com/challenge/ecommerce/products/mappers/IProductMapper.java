package com.challenge.ecommerce.products.mappers;

import com.challenge.ecommerce.products.controllers.dto.ProductCreateDto;
import com.challenge.ecommerce.products.controllers.dto.ProductResponse;
import com.challenge.ecommerce.products.controllers.dto.ProductUpdateDto;
import com.challenge.ecommerce.products.models.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IProductMapper {
  ProductEntity productCreateDtoToEntity(ProductCreateDto request);

  ProductResponse productEntityToDto(ProductEntity entity);

  ProductEntity updateProductFromDto(
      ProductUpdateDto request, @MappingTarget ProductEntity oldEntity);
}
