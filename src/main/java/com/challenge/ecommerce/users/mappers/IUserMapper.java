package com.challenge.ecommerce.users.mappers;

import com.challenge.ecommerce.users.controllers.dtos.*;
import com.challenge.ecommerce.users.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IUserMapper {
  @Mapping(target = "password", ignore = true)
  UserEntity userCreateDtoToEntity(UserCreateRequest userCreateRequest);

  @Mapping(target = "password", ignore = true)
  @Mapping(target = "role", ignore = true)
  UserEntity adminCreateUserDtoToEntity(AdminCreateUserRequest adminCreateUserRequest);

  @Mapping(target = "password", ignore = true)
  @Mapping(target = "role",ignore = true)
  UserEntity adminUpdateUserDtoToEntity(
    @MappingTarget  UserEntity user, AdminUpdateUserRequest adminUpdateUserRequest);

  UserGetResponse userEntityToUserGetResponse(UserEntity userEntity);

  @Mapping(target = "password", ignore = true)
  UserEntity userUpdateDtoToEntity(
      @MappingTarget UserEntity userEntity, UserUpdateRequest userUpdateRequest);
}
