package com.challenge.ecommerce.users.controllers.dtos;

import com.challenge.ecommerce.utils.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserGetResponse {

  String  id;

  String name;

  String email;

  String avatarLink;

  Role role;

  LocalDateTime createdAt;
}
