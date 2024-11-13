package com.challenge.ecommerce.products.controllers.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOptionCreateDto {
  @NotBlank(message = "Option ID must not be null")
  String idOption;

  @NotBlank(message = "Option Value ID must not be null")
  String idOptionValue;
}
