package com.challenge.ecommerce.products.controllers.dto;

import com.challenge.ecommerce.categories.controllers.dto.CategoryResponse;
import com.challenge.ecommerce.reviews.controllers.dto.ReviewResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

  String id;

  String title;

  String description;

  LocalDateTime createdAt;

  Integer totalFavorites;

  Integer totalRates;

  Integer totalSold;

  String slug;

  CategoryResponse category;

  List<ProductImageResponse> images;

  // List<ReviewResponse> reviews;

  VariantShortResponse variants;
}
