package com.challenge.ecommerce.users.services;

import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserCreateAddressDeliveryRequest;
import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserGetAddressDeliveryRequest;
import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserUpdateAddressDeliveryRequest;
import com.challenge.ecommerce.utils.ApiResponse;

import java.util.List;

public interface IUserDeliveryAddressService {
  ApiResponse<Void> createDeliveryAddress(UserCreateAddressDeliveryRequest request);

  ApiResponse<Void> updateDeliveryAddress(
      UserUpdateAddressDeliveryRequest request, String deliveryAddressId);

  ApiResponse<Void> deleteDeliveryAddress(String deliveryAddressId);

  ApiResponse<Void> deleteDeliveryAddressByUser(String deliveryAddressId);

  ApiResponse<List<UserGetAddressDeliveryRequest>> getAllDeliveryAddresses();

  ApiResponse<List<UserGetAddressDeliveryRequest>> getAllDeliveryAddressesById(String userId);

  ApiResponse<UserGetAddressDeliveryRequest> getDeliveryAddress(String deliveryAddressId);
}
