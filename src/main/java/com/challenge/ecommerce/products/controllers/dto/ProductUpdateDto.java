package com.challenge.ecommerce.products.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
  @Size(min = 2, max = 60, message = "Product title must be between 2 and 60 characters")
  String title;

  String description;

  String categoryId;

  BigDecimal price;

  Integer stockQuantity;

  String optionId;
  String valueId;
}
