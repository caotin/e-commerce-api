package com.challenge.ecommerce.users.services;

import com.challenge.ecommerce.authentication.controllers.dtos.AuthenticationResponse;
import com.challenge.ecommerce.users.controllers.dtos.*;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserServices {
  // User register account method
  ApiResponse<AuthenticationResponse> signUp(UserCreateRequest userCreateRequest);

  // User update detail account method
  ApiResponse<Void> updateUserDetail(UserUpdateRequest userUpdateRequest);

  // User read user profile .
  ApiResponse<UserGetResponse> getMe();

  ApiResponse<Void> adminSignUp(AdminCreateUserRequest adminCreateUserRequest);

  ApiResponse<Void> adminUpdateUserDetail(
      AdminUpdateUserRequest adminUpdateUserRequest, String userId);

  ApiResponse<Void> adminDeleteUser(AdminDeleteUserRequest adminDeleteUserRequest);

  ApiResponse<List<UserGetResponse>> getAllUser(Pageable pageable, String name);
}
