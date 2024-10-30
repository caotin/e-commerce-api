package com.challenge.ecommerce.products.controllers.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionValueResponse {
  String id;
  String value_name;
}
