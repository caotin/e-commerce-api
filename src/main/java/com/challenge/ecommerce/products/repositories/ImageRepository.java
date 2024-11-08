package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String> {
  @Query("SELECT b FROM images b WHERE b.product.id =:id AND b.deletedAt IS NULL")
  List<ImageEntity> findByIdProductAndDeletedAtIsNull(@Param("id") String id);

  @Query(
      "SELECT b FROM images b WHERE b.product.id =:id AND b.type_image = 'AVATAR' AND b.deletedAt IS NULL")
  ImageEntity findByIdProductAvatarAndDeletedAtIsNull(@Param("id") String id);

  @Modifying
  @Transactional
  @Query("UPDATE images b SET b.deletedAt=:date WHERE b.id =:imageId")
  void deleteAvatarByImageId(@Param("imageId") String imageId, @Param("date") LocalDateTime date);

  @Query(
      "SELECT b FROM images b WHERE b.product.id =:id AND b.type_image = 'THUMBNAIL' AND b.deletedAt IS NULL")
  List<ImageEntity> findByIdProductThumbnailAndDeletedAtIsNull(@Param("id") String id);
}
