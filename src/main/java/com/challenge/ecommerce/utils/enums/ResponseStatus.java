package com.challenge.ecommerce.utils.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ResponseStatus {
  SUCCESS_LOGIN(200, "Login Completed successfully !"),
  SUCCESS(200, "Operation completed successfully");

  int code;
  String message;

  ResponseStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
