package com.challenge.ecommerce.configs.security;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.utils.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) {
    ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
    response.setStatus(errorCode.getStatusCode().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ApiResponse<?> apiResponse = ApiResponse.builder().message(errorCode.getMessage()).build();
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
      response.flushBuffer();
    } catch (IOException e) {
      throw new CustomRuntimeException(ErrorCode.UNAUTHENTICATED);
    }
  }
}
