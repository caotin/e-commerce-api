package com.challenge.ecommerce.authentication.controllers;

import com.challenge.ecommerce.authentication.controllers.dtos.AuthenticationRequest;
import com.challenge.ecommerce.authentication.controllers.dtos.AuthenticationResponse;
import com.challenge.ecommerce.authentication.controllers.dtos.RefreshRequest;
import com.challenge.ecommerce.authentication.services.IAuthenticationService;
import com.challenge.ecommerce.users.controllers.dtos.UserCreateRequest;
import com.challenge.ecommerce.users.services.IUserServices;
import com.challenge.ecommerce.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
  IAuthenticationService authenticationService;
  IUserServices userServices;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
      @RequestBody @Valid AuthenticationRequest authenticationRequest) {
    var resp = authenticationService.authenticate(authenticationRequest);
    return ResponseEntity.ok().body(resp);
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
      @RequestBody @Valid UserCreateRequest userCreateRequest) {
    var resp = userServices.signUp(userCreateRequest);
    return ResponseEntity.ok().body(resp);
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout() {
    var resp = authenticationService.logout();
    return ResponseEntity.ok(resp);
  }

  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<AuthenticationResponse>> refresh(
      @RequestBody @Valid RefreshRequest request) {
    var resp = authenticationService.refreshToken(request);
    return ResponseEntity.ok(resp);
  }
}
