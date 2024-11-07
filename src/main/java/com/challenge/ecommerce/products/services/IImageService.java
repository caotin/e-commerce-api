package com.challenge.ecommerce.products.services;

import com.challenge.ecommerce.products.controllers.dto.ProductImageCreateDto;
import com.challenge.ecommerce.products.models.ProductEntity;

import java.util.List;

public interface IImageService {
  void saveImage(List<ProductImageCreateDto> list, ProductEntity product);

  void updateImage(List<ProductImageCreateDto> list, ProductEntity product);
}
