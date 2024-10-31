package com.challenge.ecommerce.products.repositories;

import com.challenge.ecommerce.products.models.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, String> {
    @Query("SELECT b FROM product_options b WHERE b.product.id =: productId AND b.deletedAt IS NULL")
    Optional<ProductOptionEntity> findByProductIDAndDeletedAtIsNull(@Param("productId") String productId);
}
