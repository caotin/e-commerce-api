package com.challenge.ecommerce.categories.controllers.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
  String id;
  String name;
  String category_img;
  String category_parent_name;
}
