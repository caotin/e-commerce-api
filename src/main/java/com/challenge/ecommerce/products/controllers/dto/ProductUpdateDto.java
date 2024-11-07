package com.challenge.ecommerce.products.controllers.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateDto {
  @NotBlank(message = "Product title cannot be blank")
  @Size(min = 2, max = 60, message = "Product title must be between 2 and 60 characters")
  String title;

  String description;

  String categoryId;

  BigDecimal price;

  Integer stock_quantity;

  String sku_id;

  @NotEmpty(message = "Option list cannot be empty")
  @Valid
  List<ProductOptionCreateDto> options = new ArrayList<>();

  @NotEmpty(message = "Images list cannot be empty")
  @Valid
  List<ProductImageCreateDto> images = new ArrayList<>();
}
