package com.challenge.ecommerce.exceptionHandlers;

import com.challenge.ecommerce.utils.ApiResponse;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse("Validation error");
    ApiResponse<?> apiResponse = new ApiResponse<>();
    apiResponse.setMessage(message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
  }

  @ExceptionHandler(value = IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
    ApiResponse<?> apiResponse = new ApiResponse<>();
    apiResponse.setMessage(e.getMessage());
    return ResponseEntity.badRequest().body(apiResponse);
  }
}
