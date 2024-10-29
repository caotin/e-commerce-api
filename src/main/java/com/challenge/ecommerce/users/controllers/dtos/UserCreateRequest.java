package com.challenge.ecommerce.users.controllers.dtos;

import jakarta.validation.constraints.Email;
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
public class UserCreateRequest {
  @NotNull(message = "password  must be not null")
  @Size(min = 8, message = "Password length must be at least 8 characters")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&!+=])(?=\\S+$).{8,}$",
      message =
          "Password must be at least 8 characters long, and contain at least one digit, one lowercase letter, one uppercase letter, and one special character, with no whitespace.")
  String password;

  @NotNull(message = "confirmPassword must be not null")
  String confirmPassword;

  @NotNull(message = "email must be not null")
  @Email(message = "Invalid email format")
  String email;
}
