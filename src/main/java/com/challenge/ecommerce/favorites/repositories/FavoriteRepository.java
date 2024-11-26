package com.challenge.ecommerce.favorites.repositories;

import com.challenge.ecommerce.favorites.models.FavoriteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteRepository
    extends JpaRepository<FavoriteEntity, String>, JpaSpecificationExecutor<FavoriteEntity> {
  @Query(
      "SELECT COUNT(c) > 0 FROM favorites c WHERE c.product.id = :productId AND c.user.id = :userId AND c.deletedAt IS NULL")
  Boolean existsByProductIdAndUserAndDeletedAtIsNull(
      @Param("productId") String productId, @Param("userId") String userId);

  @Query(
      "SELECT b FROM favorites b WHERE b.product.id =:productId AND b.user.id=:userId AND b.deletedAt IS NULL")
  Optional<FavoriteEntity> findByProductIdAndUserIdAndDeletedAtIsNull(
      @Param("productId") String productId, @Param("userId") String userId);

  @Query("SELECT b FROM favorites b WHERE b.user.id = :userId AND b.deletedAt IS NULL")
  Page<FavoriteEntity> findByUserIdAndDeletedAtIsNull(
      @Param("userId") String userId, Pageable pageable);

  @Query("SELECT COUNT(b) FROM favorites b WHERE b.product.id =:productId AND b.deletedAt IS NULL")
  Integer findAllFavoriteByProductIdAndDeletedAtIsNull(@Param("productId") String productId);
}
