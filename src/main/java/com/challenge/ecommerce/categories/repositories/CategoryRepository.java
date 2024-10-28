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

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
  @Query("SELECT COUNT(c) > 0 FROM categories c WHERE c.name = :name AND c.deletedAt IS NULL")
  Boolean existsByNameAndDeletedAtIsNull(@Param("name") String name);

  @Query("SELECT b FROM categories b WHERE b.deletedAt IS NULL")
  Page<CategoryEntity> findAllByDeletedAtIsNull(Pageable pageable);

  @Query("SELECT b FROM categories b WHERE b.id = :categoryId AND b.deletedAt IS NULL")
  Optional<CategoryEntity> findByIdAndDeletedAt(@Param("categoryId") String categoryId);

  @Query("SELECT b FROM categories b WHERE b.slug = :categorySlug AND b.deletedAt IS NULL")
  Optional<CategoryEntity> findBySlugAndDeletedAt(@Param("categorySlug") String categorySlug);

  @Query(
      "SELECT b FROM categories b WHERE b.parentCategory.slug = :categoryParentSlug AND b.deletedAt IS NULL")
  Page<CategoryEntity> findByParentSlugAndDeletedAt(
      @Param("categoryParentSlug") String categoryParentSlug, Pageable pageable);

  @Query(
      "SELECT b FROM categories b WHERE b.parentCategory.id = :categoryId AND b.deletedAt IS NULL")
  List<CategoryEntity> findByParentIdAndDeletedAt(@Param("categoryId") String categoryId);
}
