package com.challenge.ecommerce.utils.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ResponseStatus {
  SUCCESS_LOGIN("Login Completed successfully !"),
  SUCCESS("Operation completed successfully"),
  SUCCESS_SIGNUP("Signup completed successfully"),
  SUCCESS_UPDATE("Update completed successfully");

  String message;

  ResponseStatus(String message) {
    this.message = message;
  }
}
