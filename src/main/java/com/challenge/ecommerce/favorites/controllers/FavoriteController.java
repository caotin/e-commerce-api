package com.challenge.ecommerce.favorites.controllers;

import com.challenge.ecommerce.favorites.services.IFavoriteService;
import com.challenge.ecommerce.utils.ApiResponse;
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
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FavoriteController {
  IFavoriteService favoriteService;

  static final String DEFAULT_FILTER_PAGE = "1";
  static final String DEFAULT_FILTER_SIZE = "10";
  static final Sort DEFAULT_FILTER_SORT = Sort.by(Sort.Direction.DESC, "createdAt");
  static final Sort DEFAULT_FILTER_SORT_ASC = Sort.by(Sort.Direction.ASC, "createdAt");

  @PostMapping("/{productId}")
  public ResponseEntity<?> addFavorite(@PathVariable("productId") String productId) {
    favoriteService.addFavorite(productId);
    var resp = ApiResponse.builder().message("Add product in favorite list successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<?> getFavoriteByProductIdUser(@PathVariable("productId") String productId) {
    var favorite = favoriteService.getFavoriteByProductIdUser(productId);
    var resp = ApiResponse.builder().result(favorite).message("Get favorite successfully").build();
    return ResponseEntity.ok(resp);
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<?> removeFavorite(@PathVariable("productId") String productId) {
    favoriteService.removeFavorite(productId);
    var resp = ApiResponse.builder().message("Remove favorite successfully").build();
    return ResponseEntity.ok(resp);
  }

  @GetMapping
  public ResponseEntity<?> getAllFavoritesByUser(
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) @Min(1) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) @Min(1) int size,
      @RequestParam(required = false) String sortParam) {
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page - 1, size, sort);
    var listFavorites = favoriteService.getAllFavoriteByUser(pageable);
    return ResponseEntity.ok(listFavorites);
  }

  @GetMapping("/getAll")
  public ResponseEntity<?> getAllFavorites(
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_PAGE) @Min(1) int page,
      @RequestParam(required = false, defaultValue = DEFAULT_FILTER_SIZE) @Min(1) int size,
      @RequestParam(required = false) String sortParam,
      @RequestParam(required = false) String userName,
      @RequestParam(required = false) String productName){
    Sort sort = DEFAULT_FILTER_SORT;
    if (sortParam != null && sortParam.equalsIgnoreCase("ASC")) {
      sort = DEFAULT_FILTER_SORT_ASC;
    }
    Pageable pageable = PageRequest.of(page - 1, size, sort);
    var listAllFavorites = favoriteService.getAllFavorite(pageable, userName, productName);
    return ResponseEntity.ok(listAllFavorites);
  }
}
