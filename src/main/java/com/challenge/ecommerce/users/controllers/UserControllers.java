package com.challenge.ecommerce.users.controllers;

import com.challenge.ecommerce.users.controllers.dtos.UserGetResponse;
import com.challenge.ecommerce.users.controllers.dtos.UserUpdateRequest;
import com.challenge.ecommerce.users.services.impl.UserService;
import com.challenge.ecommerce.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserControllers {
  UserService userService;

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<UserGetResponse>> getMyInformation() {
    var resp = userService.getMe();
    return ResponseEntity.ok().body(resp);
  }

  @PutMapping("/me")
  public ResponseEntity<ApiResponse<Void>> updateMyInformation(
      @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
    var resp = userService.updateUserDetail(userUpdateRequest);
    return ResponseEntity.ok().body(resp);
  }
}
