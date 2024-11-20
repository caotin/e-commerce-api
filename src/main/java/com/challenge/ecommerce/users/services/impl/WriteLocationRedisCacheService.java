package com.challenge.ecommerce.users.services.impl;

import com.challenge.ecommerce.users.models.location.District;
import com.challenge.ecommerce.users.models.location.Province;
import com.challenge.ecommerce.users.models.location.Ward;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WriteLocationRedisCacheService {
  LocationApiService locationApiService;
  RedisTemplate<String, Object> redisTemplate;
@PostConstruct
  public void cacheAllLocation() {
    if (redisTemplate.opsForHash().size("provinces") == 0) {
      List<Province> provinces = locationApiService.getAllProvinces();
      provinces.forEach(
          province -> {
            redisTemplate
                .opsForHash()
                .put("provinces", String.valueOf(province.getCode()), province.getName());
            List<District> districts =
                locationApiService.getDistrictsByProvince(province.getCode());
            districts.forEach(
                district -> {
                  redisTemplate
                      .opsForHash()
                      .put(
                          "districts:" + province.getCode(),
                          String.valueOf(district.getCode()),
                          district.getName());
                  List<Ward> wards = locationApiService.getWardsByDistrict(district.getCode());
                  wards.forEach(
                      ward ->
                          redisTemplate
                              .opsForHash()
                              .put(
                                  "wards:" + district.getCode(),
                                  String.valueOf(ward.getCode()),
                                  ward.getName()));
                });
          });
    }
  }
}
