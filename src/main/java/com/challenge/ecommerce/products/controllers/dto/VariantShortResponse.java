package com.challenge.ecommerce.products.controllers.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantShortResponse {
  String id;
  String sku_id;
  Integer stock_quantity;
  BigDecimal price;
  LocalDateTime created_at;
  // Option
}
