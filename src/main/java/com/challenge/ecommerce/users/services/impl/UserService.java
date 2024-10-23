package com.challenge.ecommerce.users.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.users.controllers.dtos.UserCreatRequest;
import com.challenge.ecommerce.users.controllers.dtos.UserGetResponse;
import com.challenge.ecommerce.users.controllers.dtos.UserUpdateRequest;
import com.challenge.ecommerce.users.mappers.IUserMapper;
import com.challenge.ecommerce.users.repositories.UserRepository;
import com.challenge.ecommerce.users.services.IUserServices;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.AuthUtils;
import com.challenge.ecommerce.utils.enums.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserServices {
  UserRepository userRepository;
  IUserMapper userMapper;
  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

  @Override
  public ApiResponse<Void> signUp(UserCreatRequest userCreatRequest) {
    if (userRepository.existsByEmail(userCreatRequest.getEmail())) {
      throw new CustomRuntimeException(ErrorCode.EMAIL_EXISTED);
    }
    if (!userCreatRequest.getPassword().equals(userCreatRequest.getConfirmPassword())) {
      throw new CustomRuntimeException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCH);
    }
    var user = userMapper.userCreateDtoToEntity(userCreatRequest);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setName("user_" + UUID.randomUUID().toString().substring(0, 5));
    userRepository.save(user);
    return ApiResponse.<Void>builder().message(ResponseStatus.SUCCESS_SIGNUP.getMessage()).build();
  }

  @Override
  @Transactional
  public ApiResponse<Void> updateUserDetail(UserUpdateRequest userUpdateRequest) {
    if (userUpdateRequest.getEmail() != null) {
      if (!userRepository.existsByEmail(userUpdateRequest.getEmail())) {
        throw new CustomRuntimeException(ErrorCode.EMAIL_EXISTED);
      }
    } else if (userUpdateRequest.getName() != null) {
      if (!userRepository.existsByName(userUpdateRequest.getName())) {
        throw new CustomRuntimeException(ErrorCode.USERNAME_ALREADY_EXISTS);
      }
    }
    var oldUser =
        userRepository
            .findByEmail(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    var user = userMapper.userUpdateDtoToEntity(oldUser, userUpdateRequest);
    if (userUpdateRequest.getOldPassword() != null) {
      if (!passwordEncoder.matches(userUpdateRequest.getOldPassword(), oldUser.getPassword())) {
        throw new CustomRuntimeException(ErrorCode.PASSWORD_INCORRECT);
      } else if (!userUpdateRequest
          .getConfirmPassword()
          .equals(userUpdateRequest.getNewPassword())) {
        throw new CustomRuntimeException(ErrorCode.PASSWORD_INCORRECT);
      }
      user.setPassword(passwordEncoder.encode(userUpdateRequest.getNewPassword()));
    } else {
      if (userUpdateRequest.getNewPassword() != null
          || userUpdateRequest.getConfirmPassword() != null) {
        throw new CustomRuntimeException(ErrorCode.PASSWORD_INCORRECT);
      }
    }
    userRepository.save(user);
    return ApiResponse.<Void>builder().message(ResponseStatus.SUCCESS_UPDATE.getMessage()).build();
  }

  @Override
  public ApiResponse<UserGetResponse> getMe() {
    var user =
        userRepository
            .findByEmail(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));

    return ApiResponse.<UserGetResponse>builder()
        .result(userMapper.userEntityToUserGetResponse(user))
        .build();
  }
}
