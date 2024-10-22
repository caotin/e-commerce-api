package com.challenge.ecommerce.exceptionHandlers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomRuntimeException extends RuntimeException {
  private ErrorCode errorCode;

  public CustomRuntimeException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
