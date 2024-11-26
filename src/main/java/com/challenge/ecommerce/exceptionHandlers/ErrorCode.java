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
  SELF_EMAIL_DUPLICATION("Cannot use your own email", HttpStatus.BAD_REQUEST),
  SELF_NAME_DUPLICATION("Cannot use your own name", HttpStatus.BAD_REQUEST),
  EMAIL_EXISTED("Email already existed", HttpStatus.BAD_REQUEST),
  PASSWORD_SHOULD_NOT_MATCH_OLD(
      "New password should not be the same as the old password", HttpStatus.BAD_REQUEST),
  // another error
  URL_NOT_EXIST("The requested URL does not exist.", HttpStatus.NOT_FOUND),
  PAGE_SIZE_POSITIVE("The page size must be greater than 0", HttpStatus.BAD_REQUEST),
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
  OPTION_NAME_EXISTED("Option name already existed", HttpStatus.BAD_REQUEST),
  OPTION_VALUE_NAME_EXISTED("Option value name already existed", HttpStatus.BAD_REQUEST),
  OPTION_NOT_FOUND("Option not found", HttpStatus.NOT_FOUND),
  PROVINCE_NOT_FOUND("The province you selected does not exist", HttpStatus.BAD_REQUEST),
  DISTRICT_NOT_FOUND("The district you selected does not exist", HttpStatus.BAD_REQUEST),
  WARD_NOT_FOUND("The ward you selected does not exist", HttpStatus.BAD_REQUEST),
  MIN_PRICE_GREATER_MAX_PRICE("maxPrice must be greater than minPrice", HttpStatus.BAD_REQUEST),
  SKU_ID_EXISTED("Sku id already existed", HttpStatus.BAD_REQUEST),
  OPTION_VALUE_NOT_FOUND("Option value not found", HttpStatus.BAD_REQUEST),
  VARIANT_NOT_FOUND("Variant not found", HttpStatus.NOT_FOUND),
  PRODUCT_NAME_EXISTED("Product name already existed", HttpStatus.BAD_REQUEST),
  INVALID_IMAGE_URL("Invalid image URL", HttpStatus.BAD_REQUEST),
  // address delivery
  ADDRESS_NOT_FOUND("Address not found!", HttpStatus.NOT_FOUND),
  NOT_UPDATE_ALL_LOCATION(
      "You must update the entire address information!", HttpStatus.BAD_REQUEST),
  NOT_UPDATE_REQUEST("You have not updated anything!", HttpStatus.NOT_FOUND),
  // order_item
  ORDER_ITEM_NOT_FOUND("Order item not found", HttpStatus.NOT_FOUND),
  ORDER_ITEM_ALREADY_REVIEWED("You have already reviewed this order item", HttpStatus.BAD_REQUEST),
  // order
  ORDER_NOT_FOUND("Order not found", HttpStatus.NOT_FOUND),
  NO_PERMISSION_TO_REVIEW("You do not have permission to review", HttpStatus.FORBIDDEN),
  // review
  REVIEW_NOT_FOUND("Review not found", HttpStatus.NOT_FOUND),
  EMPTY_UPDATE_REVIEW_CONTENT("Review content cannot be empty!", HttpStatus.BAD_REQUEST),
  NOT_YOUR_REVIEW("This review does not belong to you!", HttpStatus.FORBIDDEN),
  PRODUCT_NOT_FOUND("Product not found", HttpStatus.NOT_FOUND),
  DESCRIPTION_CANNOT_BE_NULL("Description cannot be null", HttpStatus.BAD_REQUEST),
  INVALID_OPTION_VALUE_FOR_OPTION("Invalid option value for option", HttpStatus.BAD_REQUEST),
  AVATAR_PRODUCT_IMAGE_ONLY_ONE("Avatar product image only one", HttpStatus.BAD_REQUEST),
  AVATAR_PRODUCT_CANNOT_BE_NULL("Avatar product image cannot be null", HttpStatus.BAD_REQUEST),
  THUMBNAIL_PRODUCT_CANNOT_BE_NULL(
      "Thumbnail product image cannot be null", HttpStatus.BAD_REQUEST),
  OPTION_NAME_CANNOT_BE_NULL("Option name cannot be null", HttpStatus.BAD_REQUEST),
  INVALID_OPTION_VALUE_NAME("Invalid option value name",HttpStatus.BAD_REQUEST)
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
