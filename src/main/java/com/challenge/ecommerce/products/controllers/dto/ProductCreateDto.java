package com.challenge.ecommerce.products.controllers.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateDto {
  @NotBlank(message = "Product title cannot be blank")
  @Size(min = 2, max = 60, message = "Product title must be between 2 and 50 characters")
  String title;

  @NotBlank(message = "Product description cannot be blank")
  @Size(min = 2, message = "Product description must be at least 2 characters")
  String description;

  @NotBlank(message = "Category Id must not be null")
  String categoryId;

  @NotNull(message = "Price must not be null")
  @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
  BigDecimal price;

  @NotNull(message = "Stock quantity must not be null")
  @Min(value = 0, message = "Stock quantity must be non-negative")
  Integer stock_quantity;

  @NotBlank(message = "Option Id must not be null")
  String option_id;

  @NotBlank(message = "Value Id must not be null")
  String value_id;

  @NotEmpty(message = "Images list cannot be empty")
  List<ProductImageCreateDto> images;
}
