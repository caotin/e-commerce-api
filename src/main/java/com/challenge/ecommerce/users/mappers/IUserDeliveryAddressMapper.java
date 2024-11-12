package com.challenge.ecommerce.users.mappers;

import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserGetAddressDeliveryRequest;
import com.challenge.ecommerce.users.models.DeliveryAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IUserDeliveryAddressMapper {
  UserGetAddressDeliveryRequest toUserGetAddressDeliveryRequest(
      DeliveryAddressEntity deliveryAddressEntity);
}
