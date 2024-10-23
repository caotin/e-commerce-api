package com.challenge.ecommerce.users.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreatRequest {
  @NotNull(message = "password  must be not null")
  @Size(min = 6, message = "Password length must be at least 6 characters")
  @Pattern(
      regexp = "^[a-zA-Z0-9@#$%^&*()!]+$",
      message = "Password must not contain spaces or accented characters")
  String password;

  @NotNull(message = "confirmPassword must be not null")
  String confirmPassword;

  @NotNull(message = "email must be not null")
  @Pattern(
      regexp = "^[a-zA-Z][a-zA-Z0-9._-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
      message = "Invalid email format. Please enter a valid email (e.g., example@domain.com)")
  String email;
}
