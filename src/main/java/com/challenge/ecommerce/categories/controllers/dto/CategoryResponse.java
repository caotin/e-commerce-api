package com.challenge.ecommerce.categories.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
  @NotNull String id;

  @NotBlank String name;

  @URL String category_img;

  @NotBlank String slug;

  int productStock;

  CategoryParentResponse parentCategory;

  List<CategoryResponse> childCategories;
}
