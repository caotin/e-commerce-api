package com.challenge.ecommerce.users.services;

import com.challenge.ecommerce.users.controllers.dtos.UserCreateRequest;
import com.challenge.ecommerce.users.controllers.dtos.UserGetResponse;
import com.challenge.ecommerce.users.controllers.dtos.UserUpdateRequest;
import com.challenge.ecommerce.utils.ApiResponse;

public interface IUserServices {
    ApiResponse<Void> signUp(UserCreateRequest userCreateRequest);
    ApiResponse<Void> updateUserDetail(UserUpdateRequest userUpdateRequest);
    ApiResponse<UserGetResponse> getMe();
}
