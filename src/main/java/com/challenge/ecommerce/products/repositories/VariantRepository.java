package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<VariantEntity, String> {
  @Query("SELECT COUNT(c) > 0 FROM variants c WHERE c.sku_id = :skuId AND c.deletedAt IS NULL")
  Boolean existsBySkuIdAndDeletedAtIsNull(@Param("skuId") String skuId);

  @Query("SELECT b FROM variants b WHERE b.product.id =:productId AND b.deletedAt IS NULL")
  Optional<VariantEntity> findByProductIDAndDeletedAtIsNull(@Param("productId") String productId);
}
