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
  //  REFRESH_TOKEN_INVALID("Refresh token is invalid or expired.", HttpStatus.UNAUTHORIZED),
  CATEGORY_EXISTED("Category name already existed", HttpStatus.BAD_REQUEST),
  CATEGORY_NOT_FOUND("Category not found", HttpStatus.NOT_FOUND),
  SET_IMAGE_NOT_SUCCESS("Failed to upload category image", HttpStatus.BAD_REQUEST),
  CATEGORY_IMAGE_NOT_FOUND("Category image not found", HttpStatus.NOT_FOUND),
  IMAGE_NOT_SUPPORT(
      "Unsupported image format. Please use JPG, PNG, TIFF, WebP or JFIF", HttpStatus.BAD_REQUEST),
  IMAGE_NOT_FOUND("Image not found", HttpStatus.NOT_FOUND),
  SORT_NOT_SUPPORTED("Sort not supported", HttpStatus.BAD_REQUEST),
  FAILED_UPLOAD("Failed to upload image", HttpStatus.BAD_REQUEST),
  FAILED_DELETE("Failed to delete image", HttpStatus.BAD_REQUEST),
  CATEGORY_PARENT_NOT_FOUND("Category parent not found", HttpStatus.NOT_FOUND),
  CATEGORY_PARENT_FAILED_ITSELF("Category parent must not itself", HttpStatus.BAD_REQUEST),
  CATEGORY_PARENT_FAILED(
      "category is creating a ParentCategory that is a child of that category",
      HttpStatus.BAD_REQUEST),
  // Products
  MIN_PRICE_GREATER_MAX_PRICE("maxPrice must be greater than minPrice", HttpStatus.BAD_REQUEST),
  ;

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
