package com.challenge.ecommerce.utils.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ResponseStatus {
  SUCCESS_LOGIN("Login  successfully !"),
  SUCCESS("Operation completed successfully"),
  SUCCESS_SIGNUP("Signup completed successfully"),
  SUCCESS_UPDATE("Update completed successfully"),
  SUCCESS_DELETE("Delete %s successfully!");

  String message;

  ResponseStatus(String message) {
    this.message = message;
  }

  public String getFormattedMessage(String dynamicPart) {
    return String.format(this.message, dynamicPart);
  }
}
