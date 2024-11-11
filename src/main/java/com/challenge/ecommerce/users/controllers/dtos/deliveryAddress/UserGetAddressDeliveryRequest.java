package com.challenge.ecommerce.users.controllers.dtos.deliveryAddress;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetAddressDeliveryRequest {
  String id;

  String guide_position;

  String province;

  String district;

  String ward;

  LocalDateTime createdAt;

  LocalDateTime updatedAt;
}
