package com.challenge.ecommerce.users.controllers.location;

import com.challenge.ecommerce.users.models.location.BaseLocation;
import com.challenge.ecommerce.users.services.impl.LocationService;
import com.challenge.ecommerce.utils.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LocationController {
  LocationService locationService;

  @GetMapping("/provinces")
  public ResponseEntity<ApiResponse<List<BaseLocation>>> getProvinces() {
    var resp = locationService.getAllProvinces();
    return ResponseEntity.ok().body(resp);
  }

  @GetMapping("/districts/{provinceCode}")
  public ResponseEntity<ApiResponse<List<BaseLocation>>> getDistricts(
      @PathVariable int provinceCode) {
    var resp = locationService.getDistrictsByProvinceId(provinceCode);
    return ResponseEntity.ok().body(resp);
  }

  @GetMapping("/wards/{districtCode}")
  public ResponseEntity<ApiResponse<List<BaseLocation>>> getWards(@PathVariable int districtCode) {
    var resp = locationService.getWardsByDistrictId(districtCode);
    return ResponseEntity.ok().body(resp);
  }
}
