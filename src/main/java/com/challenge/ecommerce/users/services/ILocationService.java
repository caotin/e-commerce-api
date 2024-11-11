package com.challenge.ecommerce.users.services;

import com.challenge.ecommerce.users.models.location.BaseLocation;
import com.challenge.ecommerce.utils.ApiResponse;

import java.util.List;

public interface ILocationService {
  ApiResponse<List<BaseLocation>> getAllProvinces();

  ApiResponse<List<BaseLocation>> getDistrictsByProvinceId(int provinceCode);

  ApiResponse<List<BaseLocation>> getWardsByDistrictId(int districtCode);

  String getProvinceByCode(int provinceCode);

  String getDistrictByCode(int provinceCode, int districtCode);

  String getWardByCode(int districtCode, int wardCode);
}
