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
public class ProductUpdateDto {
  String title;

  String description;

  String categoryId;

  @NotEmpty(message = "Variant list cannot be empty")
  @Valid
  List<VariantUpdateDto> variants = new ArrayList<>();

  @NotEmpty(message = "Images list cannot be empty")
  @Valid
  List<ProductImageCreateDto> images = new ArrayList<>();
}
