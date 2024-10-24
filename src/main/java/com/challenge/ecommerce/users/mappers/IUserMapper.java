package com.challenge.ecommerce.users.mappers;

import com.challenge.ecommerce.users.controllers.dtos.UserCreateRequest;
import com.challenge.ecommerce.users.controllers.dtos.UserGetResponse;
import com.challenge.ecommerce.users.controllers.dtos.UserUpdateRequest;
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

  UserGetResponse userEntityToUserGetResponse(UserEntity userEntity);

  @Mapping(target = "password", ignore = true)
  UserEntity userUpdateDtoToEntity(@MappingTarget UserEntity userEntity, UserUpdateRequest userUpdateRequest);
}
