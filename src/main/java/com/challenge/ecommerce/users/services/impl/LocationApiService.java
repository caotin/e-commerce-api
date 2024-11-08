package com.challenge.ecommerce.users.services.impl;

import com.challenge.ecommerce.users.models.location.District;
import com.challenge.ecommerce.users.models.location.Province;
import com.challenge.ecommerce.users.models.location.Ward;
import com.challenge.ecommerce.users.services.ILocationApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class LocationApiService implements ILocationApiService {

  @Value("${api.location.url}")
  String apiLocationUrl;

  private final RestTemplate restTemplate = new RestTemplate();

  @Override
  public List<Province> getAllProvinces() {
    String url = apiLocationUrl + "p/";
    List<Province> provices = new ArrayList<>();
    try {
      provices =
          restTemplate
              .exchange(
                  url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Province>>() {})
              .getBody();
    } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
      log.error(e.getMessage());
    }
    return provices;
  }

  @Override
  public List<District> getDistrictsByProvince(int provinceCode) {
    String url = apiLocationUrl + "p/" + provinceCode + "?depth=2";
    Province province = new Province();
    try {
      province = restTemplate.getForObject(url, Province.class);
    } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
      log.error(e.getMessage());
    }
    return province != null ? province.getDistricts() : Collections.emptyList();
  }

  @Override
  public List<Ward> getWardsByDistrict(int districtCode) {
    String url = apiLocationUrl + "d/" + districtCode + "?depth=2";
    District district = new District();
    try {
      district = restTemplate.getForObject(url, District.class);
    } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
      log.error(e.getMessage());
    }
    return district != null ? district.getWards() : Collections.emptyList();
  }
}
