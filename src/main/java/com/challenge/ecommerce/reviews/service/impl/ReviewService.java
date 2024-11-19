package com.challenge.ecommerce.reviews.service.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.orders.repository.OrderItemRepository;
import com.challenge.ecommerce.orders.repository.OrderRepository;
import com.challenge.ecommerce.reviews.controllers.dtos.ReviewCreateRequest;
import com.challenge.ecommerce.reviews.controllers.dtos.ReviewUpdateRequest;
import com.challenge.ecommerce.reviews.mappers.IReviewMapper;
import com.challenge.ecommerce.reviews.repository.ReviewRepository;
import com.challenge.ecommerce.reviews.service.IReviewService;
import com.challenge.ecommerce.users.repositories.UserRepository;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.AuthUtils;
import com.challenge.ecommerce.utils.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService implements IReviewService {

  ReviewRepository reviewRepository;
  OrderItemRepository orderItemRepository;
  OrderRepository orderRepository;
  UserRepository userRepository;
  IReviewMapper reviewMapper;

  @Override
  public ApiResponse<Void> createReview(String order_item_id, ReviewCreateRequest request) {
    if (reviewRepository.exitReviewOfOrderItem(order_item_id)) {
      throw new CustomRuntimeException(ErrorCode.ORDER_ITEM_ALREADY_REVIEWED);
    }
    var orderItem =
        orderItemRepository
            .findActiveOrderItemByOrderId(order_item_id)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ORDER_ITEM_NOT_FOUND));
    var order =
        orderRepository
            .findOrderActiveByOrderId(orderItem.getId())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ORDER_NOT_FOUND));
    var limitTimeReview = order.getUpdatedAt().plusDays(7);
    if (!(ChronoUnit.MINUTES.between(LocalDateTime.now(), limitTimeReview) > 0)
        || !order.getStatus().equals(OrderStatus.DELIVERED)) {
      throw new CustomRuntimeException(ErrorCode.NO_PERMISSION_TO_REVIEW);
    }
    var review = reviewMapper.dtoCreateToEntity(request);
    review.setOrderItem(orderItem);
    reviewRepository.save(review);
    return ApiResponse.<Void>builder().message("Review created").build();
  }

  @Override
  @Transactional
  public ApiResponse<Void> updateReview(String reviewId, ReviewUpdateRequest request) {
    var review =
        reviewRepository
            .findReviewById(reviewId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.REVIEW_NOT_FOUND));
    if (review.getRating() == null && review.getContent() == null) {
      throw new CustomRuntimeException(ErrorCode.EMPTY_UPDATE_REVIEW_CONTENT);
    }
    reviewMapper.dtoUpdateToEntity(request, review);
    reviewRepository.save(review);
    return ApiResponse.<Void>builder().message("Review updated").build();
  }

  @Override
  public ApiResponse<Void> deleteReview(String reviewId) {
    var review =
        reviewRepository
            .findReviewById(reviewId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.REVIEW_NOT_FOUND));
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    if (!reviewRepository.existsByIdAndUserId(review.getId(), user.getId())) {
      throw new CustomRuntimeException(ErrorCode.NOT_YOUR_REVIEW);
    }
    review.setDeletedAt(LocalDateTime.now());
    reviewRepository.save(review);
    return ApiResponse.<Void>builder().message("Review deleted").build();
  }

  @Override
  public ApiResponse<Void> deleteReviewByAdmin(String reviewId) {
    var review =
        reviewRepository
            .findReviewById(reviewId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.REVIEW_NOT_FOUND));
    review.setDeletedAt(LocalDateTime.now());
    reviewRepository.save(review);
    return ApiResponse.<Void>builder().message("Review deleted").build();
  }
}
