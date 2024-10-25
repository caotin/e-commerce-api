package com.challenge.ecommerce.users.services;

import com.challenge.ecommerce.users.controllers.dtos.UserCreateRequest;
import com.challenge.ecommerce.users.controllers.dtos.UserGetResponse;
import com.challenge.ecommerce.users.controllers.dtos.UserUpdateRequest;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IUserServices {
    ApiResponse<Void> signUp(UserCreateRequest userCreateRequest);
    ApiResponse<Void> updateUserDetail(UserUpdateRequest userUpdateRequest);
    ApiResponse<UserGetResponse> getMe();
    ApiResponse<Void> updateImage(MultipartFile file);
}
