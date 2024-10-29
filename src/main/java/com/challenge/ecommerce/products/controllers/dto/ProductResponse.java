package com.challenge.ecommerce.products.controllers.dto;

import com.challenge.ecommerce.categories.controllers.dto.CategoryResponse;
import com.challenge.ecommerce.reviews.controllers.dto.ReviewResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

  int totalFavorites;

  int totalRates;

  int totalSold;

  String slug;

  CategoryResponse category;

  List<ProductImageResponse> images = new ArrayList<>();

  // List<ReviewResponse> reviews = new ArrayList<>();;

  VariantShortResponse variants;
}
