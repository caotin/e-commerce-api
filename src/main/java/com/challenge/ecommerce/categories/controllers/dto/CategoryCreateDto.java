package com.challenge.ecommerce.categories.controllers.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreateDto {
  @NotEmpty(message = "Category name must not be empty")
  String name;

  String category_parent_name;
}
