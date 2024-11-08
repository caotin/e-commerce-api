package com.challenge.ecommerce.users.services;

import com.challenge.ecommerce.users.models.location.District;
import com.challenge.ecommerce.users.models.location.Province;
import com.challenge.ecommerce.users.models.location.Ward;

import java.util.List;

public interface ILocationApiService {
  List<Province> getAllProvinces();

  List<District> getDistrictsByProvince(int provinceCode);

  List<Ward> getWardsByDistrict(int districtCode);
}
