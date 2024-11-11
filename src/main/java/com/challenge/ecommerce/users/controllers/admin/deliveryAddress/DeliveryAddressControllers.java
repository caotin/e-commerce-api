package com.challenge.ecommerce.users.controllers.admin.deliveryAddress;

import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserGetAddressDeliveryRequest;
import com.challenge.ecommerce.users.services.IUserDeliveryAddressService;
import com.challenge.ecommerce.utils.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "DeliveryAddressControllerOfAdmin")
@RequestMapping("/api/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DeliveryAddressControllers {
  IUserDeliveryAddressService userDeliveryAddressService;

  @GetMapping("/all/{userId}")
  public ResponseEntity<ApiResponse<List<UserGetAddressDeliveryRequest>>> getAll(
      @PathVariable String userId) {
    var resp = userDeliveryAddressService.getAllDeliveryAddressesById(userId);
    return ResponseEntity.ok().body(resp);
  }
}
