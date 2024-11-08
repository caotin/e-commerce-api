package com.challenge.ecommerce.users.services.impl;

import com.challenge.ecommerce.users.models.location.District;
import com.challenge.ecommerce.users.models.location.Province;
import com.challenge.ecommerce.users.models.location.Ward;
import com.challenge.ecommerce.users.services.ILocationApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class LocationApiService implements ILocationApiService {

  @Value("${api.location.url}")
  String apiLocationUrl;

  private final RestTemplate restTemplate = new RestTemplate();

  @Override
  public List<Province> getAllProvinces() {
    String url = apiLocationUrl + "p/";
    return restTemplate
        .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Province>>() {})
        .getBody();
  }

  @Override
  public List<District> getDistrictsByProvince(int provinceCode) {
    String url = apiLocationUrl + "p/" + provinceCode + "?depth=2";
    Province provide = restTemplate.getForObject(url, Province.class);
    return provide != null ? provide.getDistricts() : List.of();
  }

  @Override
  public List<Ward> getWardsByDistrict(int districtCode) {
    String url = apiLocationUrl + "d/" + districtCode + "?depth=2";
    District district = restTemplate.getForObject(url, District.class);
    return district != null ? district.getWards() : List.of();
  }
}
