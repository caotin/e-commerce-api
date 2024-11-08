package com.challenge.ecommerce.users.controllers.admin;

import com.challenge.ecommerce.users.controllers.dtos.AdminCreateUserRequest;
import com.challenge.ecommerce.users.controllers.dtos.AdminDeleteUserRequest;
import com.challenge.ecommerce.users.controllers.dtos.AdminUpdateUserRequest;
import com.challenge.ecommerce.users.controllers.dtos.UserGetResponse;
import com.challenge.ecommerce.users.services.IUserServices;
import com.challenge.ecommerce.utils.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@RestController(value = "UserControllerOfAdmin")
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserControllers {
  IUserServices userServices;
  static final String DEFAULT_FILTER_PAGE = "0";
  static final String DEFAULT_FILTER_SIZE = "10";
  static final Sort DEFAULT_FILTER_SORT = Sort.by(Sort.Direction.DESC, "createdAt");

  @GetMapping
  public ResponseEntity<ApiResponse<List<UserGetResponse>>> getAllUsers(
      @RequestParam(required = false) String name,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) String page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) String size) {
    var pages = isNumeric(page) ? Integer.parseInt(page) : 0;
    var sizes = (isNumeric(size) && !size.equals("0")) ? Integer.parseInt(size) : 10;
    Pageable pageable = PageRequest.of(pages, sizes, DEFAULT_FILTER_SORT);
    var resp = userServices.getAllUser(pageable, name);
    return ResponseEntity.ok().body(resp);
  }

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
