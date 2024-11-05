package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String> {
  @Query("SELECT b FROM images b WHERE b.product.id =:id AND b.deletedAt IS NULL")
  List<ImageEntity> findByIdProductAndDeletedAtIsNull(@Param("id") String id);
}
