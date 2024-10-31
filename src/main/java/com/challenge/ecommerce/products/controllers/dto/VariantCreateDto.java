package com.challenge.ecommerce.products.controllers.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantCreateDto {
  String sku_id;
  Integer stock_quantity;
  BigDecimal price;
}
