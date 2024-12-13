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
public class VariantUpdateDto {

  String variantId;

  @NotNull(message = "Price must not be null")
  @Positive(message = "Price must be greater than zero")
  BigDecimal price;

  @NotNull(message = "Stock quantity must not be null")
  @Min(value = 0, message = "Stock quantity must be non-negative")
  Integer stock_quantity;

  @NotBlank(message = "SkuId must not be null")
  String sku_id;

  @NotEmpty(message = "Option list cannot be empty")
  @Valid
  List<ProductOptionCreateDto> options = new ArrayList<>();
}
