package com.challenge.ecommerce.products.controllers.dto;

import com.challenge.ecommerce.categories.controllers.dto.CategoryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFavoriteResponse {
  String id;

  String title;

  String description;

  LocalDateTime createdAt;

  String slug;

  CategoryResponse category;
}
