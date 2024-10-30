package com.challenge.ecommerce.users.controllers.admin;

import com.challenge.ecommerce.users.controllers.dtos.AdminCreateUserRequest;
import com.challenge.ecommerce.users.controllers.dtos.AdminDeleteUserRequest;
import com.challenge.ecommerce.users.controllers.dtos.AdminUpdateUserRequest;
import com.challenge.ecommerce.users.services.IUserServices;
import com.challenge.ecommerce.utils.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "UserControllerOfAdmin")
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserControllers {
  IUserServices userServices;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> register(
      @RequestBody @Valid AdminCreateUserRequest request) {
    var resp = userServices.adminSignUp(request);
    return ResponseEntity.ok().body(resp);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<ApiResponse<Void>> updateUser(
      @PathVariable @NotBlank(message = "User id must be not null !") String userId,
      @RequestBody @Valid AdminUpdateUserRequest request) {
    var resp = userServices.adminUpdateUserDetail(request, userId);
    return ResponseEntity.ok().body(resp);
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse<Void>> deleteUser(
      @RequestBody @Valid AdminDeleteUserRequest request) {
    var resp = userServices.adminDeleteUser(request);
    return ResponseEntity.ok().body(resp);
  }
}
