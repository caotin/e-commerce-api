package com.challenge.ecommerce.products.controllers;

import com.challenge.ecommerce.products.controllers.dto.OptionCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionUpdateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionValueCreateDto;
import com.challenge.ecommerce.products.controllers.dto.OptionValueUpdateDto;
import com.challenge.ecommerce.products.services.IOptionService;
import com.challenge.ecommerce.products.services.IOptionValueService;
import com.challenge.ecommerce.utils.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OptionController {

  IOptionService optionService;
  IOptionValueService optionValueService;

  static final String DEFAULT_FILTER_PAGE = "1";
  static final String DEFAULT_FILTER_SIZE = "10";
  static final Sort DEFAULT_FILTER_SORT = Sort.by(Sort.Direction.DESC, "createdAt");
  static final Sort DEFAULT_FILTER_SORT_ASC = Sort.by(Sort.Direction.ASC, "createdAt");

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

  @GetMapping
  public ResponseEntity<?> getAllOptions(
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) @Min(1) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) @Min(1) int size,
      @RequestParam(required = false) String sortParam) {
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page-1, size, sort);
    var listOptions = optionService.getListOptions(pageable);
    return ResponseEntity.ok(listOptions);
  }

  @PutMapping("/{optionId}")
  public ResponseEntity<?> updateOption(
      @PathVariable("optionId") String optionId, @RequestBody @Valid OptionUpdateDto request) {
    var option = optionService.updateOption(request, optionId);
    var resp = ApiResponse.builder().result(option).message("Update option Successfully").build();
    return ResponseEntity.ok(resp);
  }

  @PutMapping("/values/{optionValueId}")
  public ResponseEntity<?> updateOptionValue(
      @PathVariable("optionValueId") String optionValueId,
      @RequestBody @Valid OptionValueUpdateDto request) {
    var optionValue = optionValueService.updateOptionValue(optionValueId, request);
    var resp =
        ApiResponse.builder()
            .result(optionValue)
            .message("Update option value Successfully")
            .build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/{optionId}")
  public ResponseEntity<?> getOption(@PathVariable("optionId") String optionId) {
    var option = optionService.getOptionById(optionId);
    var resp = ApiResponse.builder().result(option).message("Get option successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/values/{optionValueId}")
  public ResponseEntity<?> getOptionValue(@PathVariable("optionValueId") String optionValueId) {
    var optionValue = optionValueService.getOptionValue(optionValueId);
    var resp = ApiResponse.builder().result(optionValue).message("Get option value successfully").build();
    return ResponseEntity.ok(resp);
  }

  @DeleteMapping("/{optionId}")
  public ResponseEntity<?> deleteOption(@PathVariable("optionId") String optionId) {
    optionService.deleteOption(optionId);
    var resp = ApiResponse.builder().message("Delete option successfully").build();
    return ResponseEntity.ok(resp);
  }

  @DeleteMapping("/values/{optionValueId}")
  public ResponseEntity<?> deleteOptionValue(@PathVariable("optionValueId") String optionValueId) {
    optionValueService.deleteOptionValue(optionValueId);
    var resp = ApiResponse.builder().message("Delete option value successfully").build();
    return ResponseEntity.ok(resp);
  }
}
