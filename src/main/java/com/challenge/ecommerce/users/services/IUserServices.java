package com.challenge.ecommerce.users.services;

import com.challenge.ecommerce.authentication.controllers.dtos.AuthenticationResponse;
import com.challenge.ecommerce.users.controllers.dtos.*;
import com.challenge.ecommerce.utils.ApiResponse;

import java.util.List;

public interface IUserServices {
    // User register account method
    ApiResponse<AuthenticationResponse> signUp(UserCreateRequest userCreateRequest);
    // User update detail account method
    ApiResponse<Void> updateUserDetail(UserUpdateRequest userUpdateRequest);
    // User read user profile .
    ApiResponse<UserGetResponse> getMe();

    ApiResponse<Void> AdminSignUp (AdminCreateUserRequest adminCreateUserRequest);
    ApiResponse<Void> AdminUpdateUserDetail(AdminUpdateUserRequest adminUpdateUserRequest,String userId);
    ApiResponse<Void> AdminDeleteUser(List<String> ids);
}
