package com.challenge.ecommerce.users.services.impl;

import com.challenge.ecommerce.authentication.controllers.dtos.AuthenticationRequest;
import com.challenge.ecommerce.authentication.controllers.dtos.AuthenticationResponse;
import com.challenge.ecommerce.authentication.services.IAuthenticationService;
import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.users.controllers.dtos.*;
import com.challenge.ecommerce.users.mappers.IUserMapper;
import com.challenge.ecommerce.users.repositories.UserRepository;
import com.challenge.ecommerce.users.repositories.spec.UsersSpecification;
import com.challenge.ecommerce.users.services.IUserServices;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.AuthUtils;
import com.challenge.ecommerce.utils.enums.ResponseStatus;
import com.challenge.ecommerce.utils.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserServices {
  UserRepository userRepository;
  IUserMapper userMapper;
  IAuthenticationService authenticationService;
  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

  // user register account
  @Override
  public ApiResponse<AuthenticationResponse> signUp(UserCreateRequest userCreateRequest) {
    checkEmailUnique(userCreateRequest.getEmail());
    checkPasswordConfirm(userCreateRequest.getPassword(), userCreateRequest.getConfirmPassword());
    var user = userMapper.userCreateDtoToEntity(userCreateRequest);
    user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
    // create name account with UUID random
    user.setName("user_" + UUID.randomUUID().toString().substring(0, 8));
    user.setRole(Role.USER);
    userRepository.save(user);
    var auth =
        AuthenticationRequest.builder()
            .email(user.getEmail())
            .password(userCreateRequest.getPassword())
            .build();
    var resp = authenticationService.authenticate(auth);
    resp.setMessage(ResponseStatus.SUCCESS_SIGNUP.getMessage());
    return resp;
  }

  @Override
  public ApiResponse<Void> adminSignUp(AdminCreateUserRequest adminCreateUserRequest) {
    checkEmailUnique(adminCreateUserRequest.getEmail());
    checkPasswordConfirm(
        adminCreateUserRequest.getNewPassword(), adminCreateUserRequest.getConfirmPassword());
    var user = userMapper.adminCreateUserDtoToEntity(adminCreateUserRequest);
    user.setPassword(passwordEncoder.encode(adminCreateUserRequest.getNewPassword()));
    user.setName("user_" + UUID.randomUUID().toString().substring(0, 8));
    user.setRole(
        adminCreateUserRequest.getRole().equals(Role.ADMIN.toString()) ? Role.ADMIN : Role.USER);
    return ApiResponse.<Void>builder().message(ResponseStatus.SUCCESS_SIGNUP.getMessage()).build();
  }

  @Override
  @Transactional
  public ApiResponse<Void> updateUserDetail(UserUpdateRequest userUpdateRequest) {
    var oldUser =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    if (userUpdateRequest.getName() != null && !userUpdateRequest.getName().trim().isEmpty()) {
      checkRepeatName(userUpdateRequest.getName(), oldUser.getName());
      checkNameUnique(userUpdateRequest.getName());
    }
    if (userUpdateRequest.getEmail() != null) {
      checkRepeatEmail(userUpdateRequest.getEmail(), oldUser.getEmail());
      checkEmailUnique(userUpdateRequest.getEmail());
    }
    var user = userMapper.userUpdateDtoToEntity(oldUser, userUpdateRequest);
    // check update password when password not null .
    if (userUpdateRequest.getOldPassword() != null) {
      if (!passwordEncoder.matches(userUpdateRequest.getOldPassword(), oldUser.getPassword())) {
        throw new CustomRuntimeException(ErrorCode.PASSWORD_INCORRECT);
      }
      if (userUpdateRequest.getNewPassword() == null) {
        throw new CustomRuntimeException(ErrorCode.NEW_PASSWORD_CANNOT_BE_NULL);
      }
      if (userUpdateRequest.getConfirmPassword() == null) {
        throw new CustomRuntimeException(ErrorCode.CONFIRM_PASSWORD_CANNOT_BE_NULL);
      }
      if (userUpdateRequest.getNewPassword().equals(userUpdateRequest.getOldPassword())) {
        throw new CustomRuntimeException(ErrorCode.PASSWORD_SHOULD_NOT_MATCH_OLD);
      }
      checkPasswordConfirm(
          userUpdateRequest.getNewPassword(), userUpdateRequest.getConfirmPassword());
      user.setPassword(passwordEncoder.encode(userUpdateRequest.getNewPassword()));
    }
    userRepository.save(user);
    return ApiResponse.<Void>builder().message(ResponseStatus.SUCCESS_UPDATE.getMessage()).build();
  }

  @Override
  public ApiResponse<UserGetResponse> getMe() {
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.UNAUTHENTICATED));

    return ApiResponse.<UserGetResponse>builder()
        .result(userMapper.userEntityToUserGetResponse(user))
        .build();
  }

  @Override
  @Transactional
  public ApiResponse<Void> adminUpdateUserDetail(
      AdminUpdateUserRequest adminUpdateUserRequest, String userId) {
    var oldUser =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    if (adminUpdateUserRequest.getName() != null
        && !adminUpdateUserRequest.getName().trim().isEmpty()) {
      checkRepeatName(adminUpdateUserRequest.getName(), oldUser.getName());
      checkNameUnique(adminUpdateUserRequest.getName());
    }
    if (adminUpdateUserRequest.getEmail() != null) {
      checkRepeatEmail(adminUpdateUserRequest.getEmail(), oldUser.getEmail());
      checkEmailUnique(adminUpdateUserRequest.getEmail());
    }

    // check update password when password not null .
    if (adminUpdateUserRequest.getNewPassword() != null) {
      if (passwordEncoder.matches(adminUpdateUserRequest.getNewPassword(), oldUser.getPassword())) {
        throw new CustomRuntimeException(ErrorCode.PASSWORD_SHOULD_NOT_MATCH_OLD);
      }
      if (adminUpdateUserRequest.getConfirmPassword() == null)
        throw new CustomRuntimeException(ErrorCode.CONFIRM_PASSWORD_CANNOT_BE_NULL);
      checkPasswordConfirm(
          adminUpdateUserRequest.getNewPassword(), adminUpdateUserRequest.getConfirmPassword());
      oldUser.setPassword(passwordEncoder.encode(adminUpdateUserRequest.getNewPassword()));
    }
    var newUser = userMapper.adminUpdateUserDtoToEntity(oldUser, adminUpdateUserRequest);
    newUser.setRole(Role.USER);
    if (adminUpdateUserRequest.getRole() != null) {
      if (adminUpdateUserRequest.getRole().equals(Role.ADMIN.toString())) {
        newUser.setRole(Role.ADMIN);
      }
    }
    userRepository.save(newUser);
    return ApiResponse.<Void>builder().message(ResponseStatus.SUCCESS_UPDATE.getMessage()).build();
  }

  @Override
  @Transactional
  public ApiResponse<Void> adminDeleteUser(AdminDeleteUserRequest adminDeleteUserRequest) {
    StringJoiner joiner = new StringJoiner(" ");
    for (String id : adminDeleteUserRequest.getIds()) {
      var user =
          userRepository
              .findById(id)
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
      user.setDeletedAt(LocalDateTime.now());
      userRepository.save(user);
      joiner.add(user.getName());
    }
    return ApiResponse.<Void>builder()
        .message(ResponseStatus.SUCCESS_DELETE.getFormattedMessage(joiner.toString()))
        .build();
  }

  @Override
  public ApiResponse<List<UserGetResponse>> getAllUser(Pageable pageable, String name) {
    var users =
        (name != null)
            ? userRepository.findAll(
                Specification.where(UsersSpecification.hasName(name))
                    .and(UsersSpecification.isNotDeleted()),
                pageable)
            : userRepository.findAllByDeletedAtIsNull(pageable);
    var listUsers = users.stream().map(userMapper::userEntityToUserGetResponse).toList();
    return ApiResponse.<List<UserGetResponse>>builder()
        .limit(users.getNumberOfElements())
        .page(pageable.getPageNumber())
        .total(users.getTotalElements())
        .totalPages(users.getTotalPages())
        .result(listUsers)
        .build();
  }

  // check email unique .
  private void checkEmailUnique(String email) {
    if (userRepository.findActiveUserEmails(email)) {
      throw new CustomRuntimeException(ErrorCode.EMAIL_EXISTED);
    }
  }

  private void checkRepeatEmail(String newEmail, String oldEmail) {
    if (newEmail.equals(oldEmail)) {
      throw new CustomRuntimeException(ErrorCode.SELF_EMAIL_DUPLICATION);
    }
  }

  private void checkRepeatName(String newName, String oldName) {
    if (newName.equals(oldName)) {
      throw new CustomRuntimeException(ErrorCode.SELF_NAME_DUPLICATION);
    }
  }

  // check user name unique .
  private void checkNameUnique(String name) {
    if (userRepository.findActiveUserName(name)) {
      throw new CustomRuntimeException(ErrorCode.USERNAME_ALREADY_EXISTS);
    }
  }

  // check password confirm exactly .
  private void checkPasswordConfirm(String newPassword, String confirmPassword) {
    if (!newPassword.equals(confirmPassword)) {
      throw new CustomRuntimeException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCH);
    }
  }
}
