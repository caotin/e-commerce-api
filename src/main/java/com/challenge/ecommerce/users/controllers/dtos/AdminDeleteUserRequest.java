package com.challenge.ecommerce.users.controllers.dtos;

import com.challenge.ecommerce.utils.components.customannotation.NotBlankIds;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminDeleteUserRequest {
  @NotBlankIds List<String> ids = new ArrayList<>();
}
