package com.challenge.ecommerce.reviews.service;

import com.challenge.ecommerce.reviews.controllers.dtos.ReviewCreateRequest;
import com.challenge.ecommerce.reviews.controllers.dtos.ReviewUpdateRequest;
import com.challenge.ecommerce.utils.ApiResponse;

public interface IReviewService {
  ApiResponse<Void> createReview(String order_item_id, ReviewCreateRequest response);

  ApiResponse<Void> updateReview(String reviewId, ReviewUpdateRequest request);

  ApiResponse<Void> deleteReview(String reviewId);

  ApiResponse<Void> deleteReviewByAdmin(String reviewId);
}
