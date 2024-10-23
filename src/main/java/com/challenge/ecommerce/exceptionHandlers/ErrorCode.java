package com.challenge.ecommerce.exceptionHandlers;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  UNAUTHENTICATED("Unauthorized access. Please log in.", HttpStatus.UNAUTHORIZED),
  USER_NOT_FOUND("user not found !", HttpStatus.NOT_FOUND),
  PASSWORD_INCORRECT("Password incorrect, please enter another password", HttpStatus.UNAUTHORIZED),
  TOKEN_CREATION_FAILED("Token creation failed: bad request", HttpStatus.BAD_REQUEST),
  USER_EXISTED("user already existed", HttpStatus.BAD_REQUEST),
  REFRESH_TOKEN_FAILED("refresh token failed: bad request", HttpStatus.BAD_REQUEST),
  URL_NOT_EXIST("The requested URL does not exist.", HttpStatus.NOT_FOUND),
  PAGE_SIZE_POSITIVE("The page size must be greater than 0", HttpStatus.BAD_REQUEST),
  REFRESH_TOKEN_INVALID("Refresh token is invalid or expired.", HttpStatus.UNAUTHORIZED);

  private final String message;
  private final HttpStatus statusCode;

  public int getCode() {
    return statusCode.value();
  }

  ErrorCode(String message, HttpStatus statusCode) {
    this.message = message;
    this.statusCode = statusCode;
  }
}
