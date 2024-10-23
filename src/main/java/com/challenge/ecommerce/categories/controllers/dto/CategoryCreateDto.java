package com.challenge.ecommerce.categories.controllers.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreateDto {
  @NotNull(message = "Category_name must not be null")
  @Size(min = 1)
  String name;

  String category_parent_name;
}
