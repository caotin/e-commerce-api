package com.challenge.ecommerce.products.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.products.controllers.dto.ProductImageCreateDto;
import com.challenge.ecommerce.products.models.ImageEntity;
import com.challenge.ecommerce.products.models.ProductEntity;
import com.challenge.ecommerce.products.repositories.ImageRepository;
import com.challenge.ecommerce.products.services.IImageService;
import com.challenge.ecommerce.utils.enums.TypeImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ImageServiceImpl implements IImageService {
  ImageRepository imageRepository;

  @Override
  public void updateImage(List<ProductImageCreateDto> list, ProductEntity product) {
    var productAvatar = imageRepository.findByIdProductAvatarAndDeletedAtIsNull(product.getId());
    var listProductThumbnail =
        imageRepository.findByIdProductThumbnailAndDeletedAtIsNull(product.getId());

    // Collect existing thumbnail URLs
    Set<String> existingThumbnailUrls =
        listProductThumbnail.stream().map(ImageEntity::getImages_url).collect(Collectors.toSet());

    // Collect and check requested avatar, thumbnail URLs
    String newAvatarUrl = null;
    Set<String> newThumbnailUrls = new HashSet<>();
    newAvatarUrl = collectUrlImages(list, newAvatarUrl, newThumbnailUrls);

    // Remove outdated thumbnails
    for (ImageEntity existingThumbnail : listProductThumbnail) {
      if (!newThumbnailUrls.contains(existingThumbnail.getImages_url())) {
        removeThumbnailImage(existingThumbnail);
      }
    }

    // Prepare list of new images to save
    List<ImageEntity> imageEntities = new ArrayList<>();

    // Check and update avatar if needed
    if (productAvatar == null || !newAvatarUrl.equals(productAvatar.getImages_url())) {
      if (productAvatar != null) {
        imageRepository.deleteAvatarByImageId(productAvatar.getId(), LocalDateTime.now());
      }
      ImageEntity avatarImage = new ImageEntity();
      avatarImage.setImages_url(newAvatarUrl);
      avatarImage.setType_image(TypeImage.AVATAR);
      avatarImage.setProduct(product);
      imageEntities.add(avatarImage);
    }

    // Add new thumbnails that aren't already saved
    for (String thumbnailUrl : newThumbnailUrls) {
      if (!existingThumbnailUrls.contains(thumbnailUrl)) {
        ImageEntity thumbnailImage = new ImageEntity();
        thumbnailImage.setImages_url(thumbnailUrl);
        thumbnailImage.setType_image(TypeImage.THUMBNAIL);
        thumbnailImage.setProduct(product);
        imageEntities.add(thumbnailImage);
      }
    }

    // Save new images
    imageRepository.saveAll(imageEntities);
  }

  // Method to soft-delete a thumbnail by setting `deletedAt`
  void removeThumbnailImage(ImageEntity image) {
    image.setDeletedAt(LocalDateTime.now());
    imageRepository.save(image);
  }

  String collectUrlImages(
      List<ProductImageCreateDto> list, String newAvatarUrl, Set<String> newThumbnailUrls) {
    for (ProductImageCreateDto dto : list) {
      if (dto.getType_image() == TypeImage.AVATAR) {
        if (newAvatarUrl != null) {
          throw new CustomRuntimeException(ErrorCode.AVATAR_PRODUCT_IMAGE_ONLY_ONE);
        }
        newAvatarUrl = dto.getImages_url();
      } else if (dto.getType_image() == TypeImage.THUMBNAIL) {
        newThumbnailUrls.add(dto.getImages_url());
      }
    }

    // Avatar validations
    if (newAvatarUrl == null) {
      throw new CustomRuntimeException(ErrorCode.AVATAR_PRODUCT_CANNOT_BE_NULL);
    }

    // Thumbnail validations
    if (newThumbnailUrls.isEmpty()) {
      throw new CustomRuntimeException(ErrorCode.THUMBNAIL_PRODUCT_CANNOT_BE_NULL);
    }
    return newAvatarUrl;
  }
}
