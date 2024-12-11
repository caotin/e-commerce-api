package com.challenge.ecommerce.products.controllers.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateDto {
  @NotBlank(message = "Product title cannot be blank")
  @Size(min = 2, max = 60, message = "Product title must be between 2 and 60 characters")
  String title;

  @NotBlank(message = "Product description cannot be blank")
  @Size(min = 2, message = "Product description must be at least 2 characters")
  String description;

  @NotBlank(message = "Category Id must not be null")
  String categoryId;

  @NotEmpty(message = "Variant list cannot be empty")
  @Valid
  List<VariantCreateDto> variants = new ArrayList<>();

  @NotEmpty(message = "Image list cannot be empty")
  @Valid
  List<ProductImageCreateDto> images = new ArrayList<>();
}
