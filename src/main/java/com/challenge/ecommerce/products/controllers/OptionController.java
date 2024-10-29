package com.challenge.ecommerce.products.controllers;

import com.challenge.ecommerce.products.controllers.dto.OptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionValueCreateDto;
import com.challenge.ecommerce.products.services.IOptionService;
import com.challenge.ecommerce.products.services.IOptionValueService;
import com.challenge.ecommerce.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OptionController {

  IOptionService optionService;
  IOptionValueService optionValueService;

  @PostMapping
  public ResponseEntity<?> addOption(@RequestBody @Valid OptionCreateDto request) {
    var option = optionService.addOption(request);
    var resp =
        ApiResponse.builder().result(option).message("Create new option successfully").build();
    return ResponseEntity.ok(resp);
  }

  @PostMapping("/values/{optionId}")
  public ResponseEntity<?> addOptionValue(
      @PathVariable("optionId") String optionId, @RequestBody @Valid OptionValueCreateDto request) {
    var optionValue = optionValueService.addOptionValue(optionId, request);
    var resp =
        ApiResponse.builder().result(optionValue).message("Create new option successfully").build();
    return ResponseEntity.ok(resp);
  }
}
