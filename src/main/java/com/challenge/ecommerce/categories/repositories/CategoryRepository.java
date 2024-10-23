package com.challenge.ecommerce.categories.repositories;

import com.challenge.ecommerce.categories.models.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
  Boolean existsByName(String name);

  @Query("SELECT b FROM categories b WHERE b.deletedAt IS NULL")
  Page<CategoryEntity> findAllByDeletedAtIsNull(Pageable pageable);

  @Query("SELECT b FROM categories b WHERE b.id = :categoryId AND b.deletedAt IS NULL")
  Optional<CategoryEntity> findByIdAndDeletedAt(@Param("categoryId") String categoryId);

  @Transactional
  @Modifying
  @Query("UPDATE categories SET category_img = null where category_img = :imageId")
  void deleteImage(@Param("imageId") String imageId);

  @Query(
      "SELECT b FROM categories b WHERE b.category_parent_name = :categoryParentName AND b.deletedAt IS NULL")
  Page<CategoryEntity> findByParentName(
      @Param("categoryParentName") String categoryParentName, Pageable pageable);
}
