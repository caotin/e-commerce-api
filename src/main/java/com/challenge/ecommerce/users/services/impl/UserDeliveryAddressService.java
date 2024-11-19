package com.challenge.ecommerce.users.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserCreateAddressDeliveryRequest;
import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserGetAddressDeliveryRequest;
import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserUpdateAddressDeliveryRequest;
import com.challenge.ecommerce.users.mappers.IUserDeliveryAddressMapper;
import com.challenge.ecommerce.users.models.DeliveryAddressEntity;
import com.challenge.ecommerce.users.repositories.UserDeliveryAddressRepository;
import com.challenge.ecommerce.users.repositories.UserRepository;
import com.challenge.ecommerce.users.services.ILocationService;
import com.challenge.ecommerce.users.services.IUserDeliveryAddressService;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.AuthUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDeliveryAddressService implements IUserDeliveryAddressService {
  ILocationService locationService;
  UserDeliveryAddressRepository userDeliveryAddressRepository;
  UserRepository userRepository;
  IUserDeliveryAddressMapper userDeliveryAddressMapper;

  @Override
  public ApiResponse<Void> createDeliveryAddress(UserCreateAddressDeliveryRequest request) {
    var province = locationService.getProvinceByCode(request.getProvinceCode());
    var district =
        locationService.getDistrictByCode(request.getProvinceCode(), request.getDistrictCode());
    var ward = locationService.getWardByCode(request.getDistrictCode(), request.getWardCode());
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    var deliveryAddress =
        DeliveryAddressEntity.builder()
            .province(province.trim())
            .district(district.trim())
            .ward(ward.trim())
            .build();
    if (request.getGuide_position() != null) {
      deliveryAddress.setGuide_position(request.getGuide_position().trim());
    }
    deliveryAddress.setUser(user);
    userDeliveryAddressRepository.save(deliveryAddress);
    return ApiResponse.<Void>builder().message("Delivery address created").build();
  }

  @Override
  @Transactional
  public ApiResponse<Void> updateDeliveryAddress(
      UserUpdateAddressDeliveryRequest request, String deliveryAddressId) {
    if (request.getGuide_position() == null
        && request.getProvinceCode() == null
        && request.getDistrictCode() == null
        && request.getWardCode() == null) {
      throw new CustomRuntimeException(ErrorCode.NOT_UPDATE_REQUEST);
    }
    var deliveryAddress =
        userDeliveryAddressRepository
            .findDeliveryAddressActive(deliveryAddressId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ADDRESS_NOT_FOUND));
    if (request.getGuide_position() != null) {
      deliveryAddress.setGuide_position(request.getGuide_position());
    }

    if (request.getProvinceCode() != null
        || request.getDistrictCode() != null
        || request.getWardCode() != null) {
      if (request.getProvinceCode() != null
          && request.getDistrictCode() != null
          && request.getWardCode() != null) {
        var province = locationService.getProvinceByCode(request.getProvinceCode());
        var district =
            locationService.getDistrictByCode(request.getProvinceCode(), request.getDistrictCode());
        var ward = locationService.getWardByCode(request.getDistrictCode(), request.getWardCode());
        deliveryAddress.setProvince(province);
        deliveryAddress.setDistrict(district);
        deliveryAddress.setWard(ward);
      } else {
        throw new CustomRuntimeException(ErrorCode.NOT_UPDATE_ALL_LOCATION);
      }
    }

    userDeliveryAddressRepository.save(deliveryAddress);

    return ApiResponse.<Void>builder().message("Delivery address updated").build();
  }

  @Override
  @Transactional
  public ApiResponse<Void> deleteDeliveryAddress(String deliveryAddressId) {
    var deliveryAddress =
        userDeliveryAddressRepository
            .findDeliveryAddressActive(deliveryAddressId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ADDRESS_NOT_FOUND));
    deliveryAddress.setDeletedAt(LocalDateTime.now());
    userDeliveryAddressRepository.save(deliveryAddress);
    return ApiResponse.<Void>builder().message("Delivery address deleted").build();
  }

  @Override
  public ApiResponse<List<UserGetAddressDeliveryRequest>> getAllDeliveryAddresses() {
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    var resp =
        userDeliveryAddressRepository.findAllDeliveryAddressActive(user.getId()).stream()
            .map(userDeliveryAddressMapper::toUserGetAddressDeliveryRequest)
            .toList();

    return ApiResponse.<List<UserGetAddressDeliveryRequest>>builder().result(resp).build();
  }

  @Override
  public ApiResponse<List<UserGetAddressDeliveryRequest>> getAllDeliveryAddressesById(
      String userId) {
    var user =
        userRepository
            .findByIdAndNotDeleted(userId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    List<UserGetAddressDeliveryRequest> resp;
    resp =
        userDeliveryAddressRepository.findAllDeliveryAddressActive(user.getId()).stream()
            .map(userDeliveryAddressMapper::toUserGetAddressDeliveryRequest)
            .toList();
    return ApiResponse.<List<UserGetAddressDeliveryRequest>>builder().result(resp).build();
  }

  @Override
  public ApiResponse<UserGetAddressDeliveryRequest> getDeliveryAddress(String deliveryAddressId) {
    var delivery =
        userDeliveryAddressRepository
            .findDeliveryAddressActive(deliveryAddressId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ADDRESS_NOT_FOUND));
    var resp = userDeliveryAddressMapper.toUserGetAddressDeliveryRequest(delivery);
    return ApiResponse.<UserGetAddressDeliveryRequest>builder().result(resp).build();
  }

  @Override
  @Transactional
  public ApiResponse<Void> deleteDeliveryAddressByUser(String deliveryAddressId) {
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    var address =
        userDeliveryAddressRepository
            .findDeliveryAddressActive(deliveryAddressId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.ADDRESS_NOT_FOUND));
    if (!userDeliveryAddressRepository.existsByUserIdAndDeliveryId(user.getId(), address.getId())) {
      throw new CustomRuntimeException(ErrorCode.ADDRESS_NOT_FOUND);
    }
    address.setDeletedAt(LocalDateTime.now());
    userDeliveryAddressRepository.save(address);
    return ApiResponse.<Void>builder().message("Delivery address deleted").build();
  }
}
