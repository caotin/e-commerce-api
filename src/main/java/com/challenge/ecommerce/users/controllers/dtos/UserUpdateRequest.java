package com.challenge.ecommerce.users.controllers.dtos;

import com.challenge.ecommerce.utils.components.customannotation.AllowedImageFileType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest {

  String oldPassword;

  @Size(min = 8, message = "Password length must be at least 8 characters")
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!$%^&+=])(?=\\S+$).{8,}$",
      message =
          "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace")
  String newPassword;

  String confirmPassword;

  @AllowedImageFileType(
      message =
          "File extension is not supported. Allowed file types: .jpg, .png, .tiff, .webp, .jfif")
  String avatar_link;

  @Email(message = "Invalid email format")
  String email;

  @Size(min = 6, message = "username length must be 6")
  @Pattern(
      regexp = "^[a-zA-Z0-9]+$",
      message = "name must not contain special characters or accented characters")
  String name;
}
