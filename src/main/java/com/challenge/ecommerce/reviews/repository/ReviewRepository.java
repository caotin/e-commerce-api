package com.challenge.ecommerce.reviews.repository;

import com.challenge.ecommerce.reviews.models.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, String> {
  @Query("select count(re) from reviews re where re.orderItem.id=:orderItemId")
  boolean exitReviewOfOrderItem(@Param("orderItemId") String orderItemId);

  @Query("select re from reviews re where re.id = :reviewId and re.deletedAt is null")
  Optional<ReviewEntity> findReviewById(@Param("reviewId") String reviewId);

  @Query("select count(re) from reviews re where re.id =:reviewId and re.user.id=:userId ")
  boolean existsByIdAndUserId(@Param("reviewId") String reviewId, @Param("userId") String userId);
}
