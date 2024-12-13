package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<VariantEntity, String> {
  @Query("SELECT COUNT(c) > 0 FROM variants c WHERE c.sku_id = :skuId AND c.deletedAt IS NULL")
  Boolean existsBySkuIdAndDeletedAtIsNull(@Param("skuId") String skuId);

  @Query("SELECT b FROM variants b WHERE b.sku_id =:skuId AND b.deletedAt IS NULL")
  Optional<VariantEntity> findBySkuIdAndDeletedAtIsNull(@Param("skuId") String skuId);

  @Query(
      "SELECT COUNT(c) > 0 FROM variants c INNER JOIN products b ON c.product.id = b.id WHERE c.sku_id = :skuId AND c.product.id =:productId AND c.deletedAt IS NULL AND b.deletedAt IS NULL")
  Boolean existsBySkuIdAndProductIdAndDeletedAtIsNull(
      @Param("skuId") String skuId, @Param("productId") String productId);

  @Query("SELECT b FROM variants b WHERE b.product.id =:productId AND b.deletedAt IS NULL")
  List<VariantEntity> findByProductIdAndDeletedAtIsNull(@Param("productId") String productId);

  @Query("SELECT b FROM variants b WHERE b.id=:id AND b.deletedAt IS NULL")
  Optional<VariantEntity> findByIdAndDeletedAtIsNull(@Param("id") String id);
}
