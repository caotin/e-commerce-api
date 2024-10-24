package com.challenge.ecommerce.exceptionHandlers;

import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = CustomRuntimeException.class)
  public ResponseEntity<ApiResponse<?>> handleCusTomRuntimeException(CustomRuntimeException e) {
    ErrorCode errorCode = e.getErrorCode();
    ApiResponse<?> apiResponse = new ApiResponse<>();
    apiResponse.setMessage(e.getMessage());
    return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    String errorMessage =
        e.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .orElse("Invalid input data");

    ApiResponse<?> apiResponse = new ApiResponse<>();
    apiResponse.setMessage(errorMessage);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
  }
}
