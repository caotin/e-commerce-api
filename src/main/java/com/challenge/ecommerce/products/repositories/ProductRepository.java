package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository
    extends JpaRepository<ProductEntity, String>, JpaSpecificationExecutor<ProductEntity> {

  @Query("SELECT COUNT(c) > 0 FROM products c WHERE c.title = :title AND c.deletedAt IS NULL")
  Boolean existsByTitleAndDeletedAtIsNull(@Param("title") String title);

  @Query("SELECT b FROM products b WHERE b.slug=:productSlug AND b.deletedAt IS NULL")
  Optional<ProductEntity> findBySlugAndDeletedAtIsNull(@Param("productSlug") String productSlug);

  @Query("SELECT b FROM products b WHERE b.id=:productId AND b.deletedAt IS NULL")
  Optional<ProductEntity> findByIdAndDeletedAtIsNull(@Param("productId") String productId);

  @Query("SELECT b FROM products b WHERE b.category.id=:categoryId AND b.deletedAt IS NULL")
  List<ProductEntity> findByCategoryIdAndDeletedAtIsNull(@Param("categoryId") String categoryId);

  @Query(
      "SELECT c.id AS categoryId, SUM(v.stock_quantity) AS totalStock FROM categories c JOIN products p ON c.id = p.category.id LEFT JOIN variants v ON p.id = v.product.id WHERE c.id IN (:categoryIds) AND p.deletedAt IS NULL GROUP BY c.id")
  List<Object[]> findByCategoryIdsAndDeletedAtIsNull(
      @Param("categoryIds") List<String> categoryIds);
}
