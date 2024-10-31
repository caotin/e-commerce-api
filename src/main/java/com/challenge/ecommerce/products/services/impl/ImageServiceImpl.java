package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.products.controllers.dto.ProductImageCreateDto;
import com.challenge.ecommerce.products.models.ImageEntity;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.repositories.ImageRepository;
import com.challenge.ecommerce.products.services.IImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ImageServiceImpl implements IImageService {
  ImageRepository imageRepository;

  @Override
  public void saveImage(List<ProductImageCreateDto> list, ProductEntity product) {
    List<ImageEntity> imageEntities =
        list.stream()
            .map(
                child -> {
                  ImageEntity imageEntity = new ImageEntity();
                  imageEntity.setImages_url(child.getImages_url());
                  imageEntity.setType_image(child.getType_image());
                  imageEntity.setProduct(product);
                  return imageEntity;
                })
            .toList();
    imageRepository.saveAll(imageEntities);
  }
}
