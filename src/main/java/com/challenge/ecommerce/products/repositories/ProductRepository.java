package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository
    extends JpaRepository<ProductEntity, String>, JpaSpecificationExecutor<ProductEntity> {

  @Query("SELECT COUNT(c) > 0 FROM products c WHERE c.title = :title AND c.deletedAt IS NULL")
  Boolean existsByTitleAndDeletedAtIsNull(@Param("title") String title);

  @Query("SELECT b FROM products b WHERE b.slug=:productSlug AND b.deletedAt IS NULL")
  Optional<ProductEntity> findBySlugAndCreatedAtIsNull(@Param("productSlug") String productSlug);
}
