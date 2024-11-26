package com.challenge.ecommerce.categories.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreateDto {
  @NotBlank(message = "Category name must not be empty")
  String name;

  String parentCategoryId;

  String imageUrl;
}
