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
  REFRESH_TOKEN_INVALID("Refresh token is invalid or expired.", HttpStatus.UNAUTHORIZED),
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
