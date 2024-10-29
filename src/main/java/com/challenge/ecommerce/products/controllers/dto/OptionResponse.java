package com.challenge.ecommerce.products.controllers.dto;

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
public class OptionResponse {
  String id;
  String option_name;
  List<OptionValueResponse> optionValues = new ArrayList<>();
}
