package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductImageCreateDto;
import com.challenge.ecommerce.products.models.ImageEntity;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.repositories.ImageRepository;
import com.challenge.ecommerce.products.services.IImageService;
import com.challenge.ecommerce.utils.enums.TypeImage;
import com.challenge.ecommerce.utils.StringHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
                  if (!StringHelper.isValidImageUrl(child.getImages_url())) {
                    throw new CustomRuntimeException(ErrorCode.INVALID_IMAGE_URL);
                  }
                  imageEntity.setImages_url(child.getImages_url());
                  imageEntity.setType_image(child.getType_image());
                  imageEntity.setProduct(product);
                  return imageEntity;
                })
            .toList();
    imageRepository.saveAll(imageEntities);
  }

  @Override
  public void updateImage(List<ProductImageCreateDto> list, ProductEntity product) {
    var productAvatar = imageRepository.findByIdProductAvatarAndDeletedAtIsNull(product.getId());
    var listProductThumbnail =
        imageRepository.findByIdProductThumbnailAndDeletedAtIsNull(product.getId());
    List<ImageEntity> imageEntities =
        list.stream()
            .map(
                child -> {
                  ImageEntity imageEntity = new ImageEntity();
                  if (!StringHelper.isValidImageUrl(child.getImages_url())) {
                    throw new CustomRuntimeException(ErrorCode.INVALID_IMAGE_URL);
                  }
                  if (child.getType_image() == TypeImage.AVATAR) {
                    if (productAvatar == null
                        || !child.getImages_url().equals(productAvatar.getImages_url())) {
                      if (productAvatar != null) {
                        imageRepository.deleteAvatarByProductId(
                            productAvatar.getId(), LocalDateTime.now());
                      }
                      imageEntity.setImages_url(child.getImages_url());
                      imageEntity.setType_image(child.getType_image());
                      imageEntity.setProduct(product);
                    }
                  } else if (child.getType_image() == TypeImage.THUMBNAIL) {
                    if (isNewThumbnailImage(listProductThumbnail, child.getImages_url())) {
                      imageEntity.setImages_url(child.getImages_url());
                      imageEntity.setType_image(child.getType_image());
                      imageEntity.setProduct(product);
                    }
                  }
                  return imageEntity;
                })
            .filter(entity -> entity.getImages_url() != null) // Save if you have a new URL
            .toList();

    imageRepository.saveAll(imageEntities);
  }

  private boolean isNewThumbnailImage(List<ImageEntity> thumbnails, String newUrl) {
    return thumbnails.stream().noneMatch(thumbnail -> thumbnail.getImages_url().equals(newUrl));
  }
}
