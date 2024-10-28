package com.challenge.ecommerce.categories.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateDto {
  @NotBlank(message = "Category name cannot be blank")
  @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
  String name;

  String parentCategoryId;

  String imageUrl;
}
