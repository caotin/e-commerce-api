package com.challenge.ecommerce.users.controllers.dtos.deliveryAddress;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateAddressDeliveryRequest {

  @Pattern(
      regexp = "^\\s*\\S.*$",
      message = "Guide position cannot be blank or contain only whitespace")
  String guide_position;

  @Min(value = 1, message = "Province code must be a positive number")
  int provinceCode;

  @Min(value = 1, message = "District code must be a positive number")
  int districtCode;

  @Min(value = 1, message = "Ward code must be a positive number")
  int wardCode;
}
