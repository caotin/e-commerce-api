package com.challenge.ecommerce.exceptionHandlers;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  UNAUTHORIZED("Unauthorized access !", HttpStatus.FORBIDDEN),
  UNAUTHENTICATED(" Please log in !", HttpStatus.UNAUTHORIZED),
  USER_NOT_FOUND("user not found !", HttpStatus.NOT_FOUND),
  USERNAME_ALREADY_EXISTS("username already exists!", HttpStatus.BAD_REQUEST),
  PASSWORD_INCORRECT("Password incorrect, please enter another password", HttpStatus.UNAUTHORIZED),
  CONFIRM_PASSWORD_NOT_MATCH("Confirm password does not match, please try again", HttpStatus.BAD_REQUEST),
  TOKEN_CREATION_FAILED("Token creation failed: bad request", HttpStatus.BAD_REQUEST),
  EMAIL_EXISTED("email already existed", HttpStatus.BAD_REQUEST),
  REFRESH_TOKEN_FAILED("refresh token failed: bad request", HttpStatus.BAD_REQUEST),
  URL_NOT_EXIST("The requested URL does not exist.", HttpStatus.NOT_FOUND),
  PAGE_SIZE_POSITIVE("The page size must be greater than 0", HttpStatus.BAD_REQUEST),
  REFRESH_TOKEN_INVALID("Refresh token is invalid or expired.", HttpStatus.UNAUTHORIZED),
  URL_NOT_FOUND("URL not found!", HttpStatus.NOT_FOUND);

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
