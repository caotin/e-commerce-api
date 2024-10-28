package com.challenge.ecommerce.products.mappers;

import com.challenge.ecommerce.products.controllers.dto.ProductImageResponse;
import com.challenge.ecommerce.products.models.ImageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IImageMapper {
  ProductImageResponse imageEntityToDto(ImageEntity entity);
}
