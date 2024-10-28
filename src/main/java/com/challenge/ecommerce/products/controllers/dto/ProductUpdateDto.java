package com.challenge.ecommerce.products.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateDto {
  @NotBlank(message = "Product title cannot be blank")
  @Size(min = 1, max = 50, message = "Product title must be between 2 and 50 characters")
  String title;

  String description;

  String categoryId;

  BigDecimal price;

  @NotNull(message = "Stock quantity must not be null")
  Integer stock_quantity;

  String option_id;
  String value_id;
}
