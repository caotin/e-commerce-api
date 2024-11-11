package com.challenge.ecommerce.users.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.users.models.location.BaseLocation;
import com.challenge.ecommerce.users.services.ILocationService;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LocationService implements ILocationService {

  private final RedisTemplate<String, Object> redisTemplate;

  public LocationService(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public ApiResponse<List<BaseLocation>> getAllProvinces() {
    var result = new ArrayList<BaseLocation>();
    var locationMap = redisTemplate.opsForHash().entries("provinces");
    return getListApiResponse(result, locationMap);
  }

  private ApiResponse<List<BaseLocation>> getListApiResponse(
      ArrayList<BaseLocation> result, Map<Object, Object> locationMap) {
    locationMap.forEach(
        (code, name) -> {
          var provinceBase = new BaseLocation();
          provinceBase.setCode(Integer.parseInt(code.toString()));
          provinceBase.setName(name.toString());
          result.add(provinceBase);
        });

    return ApiResponse.<List<BaseLocation>>builder().result(result).build();
  }

  @Override
  public ApiResponse<List<BaseLocation>> getDistrictsByProvinceId(int provinceCode) {
    var result = new ArrayList<BaseLocation>();
    var locationMap = redisTemplate.opsForHash().entries("districts:" + provinceCode);
    if (locationMap.isEmpty()) {
      throw new CustomRuntimeException(ErrorCode.PROVINCE_NOT_FOUND);
    }
    return getListApiResponse(result, locationMap);
  }

  @Override
  public ApiResponse<List<BaseLocation>> getWardsByDistrictId(int districtCode) {
    var result = new ArrayList<BaseLocation>();
    var locationMap = redisTemplate.opsForHash().entries("wards:" + districtCode);
    if (locationMap.isEmpty()) {
      throw new CustomRuntimeException(ErrorCode.DISTRICT_NOT_FOUND);
    }
    return getListApiResponse(result, locationMap);
  }

  @Override
  public String getProvinceByCode(int provinceCode) {
    var province = redisTemplate.opsForHash().get("provinces", String.valueOf(provinceCode));
    if (province == null) {
      throw new CustomRuntimeException(ErrorCode.PROVINCE_NOT_FOUND);
    }
    return province.toString();
  }

  @Override
  public String getDistrictByCode(int provinceCode, int districtCode) {
    var district =
        redisTemplate.opsForHash().get("districts:" + provinceCode, String.valueOf(districtCode));
    if (district == null) {
      throw new CustomRuntimeException(ErrorCode.DISTRICT_NOT_FOUND);
    }
    return district.toString();
  }

  @Override
  public String getWardByCode(int districtCode, int wardCode) {
    var ward = redisTemplate.opsForHash().get("wards:" + districtCode, String.valueOf(wardCode));
    if (ward == null) {
      throw new CustomRuntimeException(ErrorCode.WARD_NOT_FOUND);
    }
    return ward.toString();
  }
}
