package com.challenge.ecommerce.products.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValueCreateDto {
  @NotBlank(message = "value name must not be null")
  String valueName;
}
