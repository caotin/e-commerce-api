package com.challenge.ecommerce.users.controllers.user.deliveryAddress;

import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserCreateAddressDeliveryRequest;
import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserGetAddressDeliveryRequest;
import com.challenge.ecommerce.users.controllers.dtos.deliveryAddress.UserUpdateAddressDeliveryRequest;
import com.challenge.ecommerce.users.services.IUserDeliveryAddressService;
import com.challenge.ecommerce.utils.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "DeliveryAddressControllerOfUser")
@RequestMapping("/api/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DeliveryAddressControllers {
  IUserDeliveryAddressService userDeliveryAddressService;

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createDeliveryAddress(
      @RequestBody @Valid UserCreateAddressDeliveryRequest request) {
    var resp = userDeliveryAddressService.createDeliveryAddress(request);
    return ResponseEntity.ok().body(resp);
  }

  @PutMapping("/{addressId}")
  public ResponseEntity<ApiResponse<Void>> updateDeliveryAddress(
      @PathVariable @NotBlank(message = "addressId id must be not null !") String addressId,
      @RequestBody @Valid UserUpdateAddressDeliveryRequest request) {
    var resp = userDeliveryAddressService.updateDeliveryAddress(request, addressId);
    return ResponseEntity.ok().body(resp);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<UserGetAddressDeliveryRequest>>> getAllDeliveryAddress() {
    var resp = userDeliveryAddressService.getAllDeliveryAddresses();
    return ResponseEntity.ok().body(resp);
  }

  @GetMapping("/{addressId}")
  public ResponseEntity<ApiResponse<UserGetAddressDeliveryRequest>> getDeliveryAddress(
      @PathVariable @NotBlank(message = "addressId id must be not null !") String addressId) {
    var resp = userDeliveryAddressService.getDeliveryAddress(addressId);
    return ResponseEntity.ok().body(resp);
  }

  @DeleteMapping("users/{addressId}")
  public ResponseEntity<ApiResponse<Void>> deleteDeliveryAddress(
      @PathVariable @NotBlank(message = "addressId id must be not null !") String addressId) {
    var resp = userDeliveryAddressService.deleteDeliveryAddressByUser(addressId);
    return ResponseEntity.ok().body(resp);
  }
}
