package com.challenge.ecommerce.exceptionHandlers;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  UNAUTHORIZED("Unauthorized access !", HttpStatus.FORBIDDEN),
  UNAUTHENTICATED(" Please log in !", HttpStatus.UNAUTHORIZED),
  REFRESH_TOKEN_FAILED("Refresh token failed.", HttpStatus.BAD_REQUEST),
  TOKEN_CREATION_FAILED("Token creation failed: bad request", HttpStatus.BAD_REQUEST),
  REFRESH_TOKEN_INVALID("Refresh token is invalid or expired.", HttpStatus.UNAUTHORIZED),
  // user detail error .
  USER_NOT_FOUND("User not found !", HttpStatus.NOT_FOUND),
  USERNAME_ALREADY_EXISTS("User Name already exists!", HttpStatus.BAD_REQUEST),
  // password error
  PASSWORD_INCORRECT("Password incorrect, please enter another password", HttpStatus.BAD_REQUEST),
  NEW_PASSWORD_CANNOT_BE_NULL("New password cannot be null", HttpStatus.BAD_REQUEST),
  CONFIRM_PASSWORD_CANNOT_BE_NULL("Confirm password cannot be null", HttpStatus.BAD_REQUEST),
  CONFIRM_PASSWORD_NOT_MATCH(
      "Confirm password does not match, please try again", HttpStatus.BAD_REQUEST),
  EMAIL_EXISTED("Email already existed", HttpStatus.BAD_REQUEST),
  PASSWORD_SHOULD_NOT_MATCH_OLD(
      "New password should not be the same as the old password", HttpStatus.BAD_REQUEST),
  // another error
  URL_NOT_EXIST("The requested URL does not exist.", HttpStatus.NOT_FOUND),
  PAGE_SIZE_POSITIVE("The page size must be greater than 0", HttpStatus.BAD_REQUEST),
  IMAGE_FILE_NOT_FOUND("The requested image file does not exist.", HttpStatus.NOT_FOUND),
  IMAGE_DELETION_FAILED("Failed to delete the image file.", HttpStatus.INTERNAL_SERVER_ERROR);;

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
