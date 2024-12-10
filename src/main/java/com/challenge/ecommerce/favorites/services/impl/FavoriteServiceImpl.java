package com.challenge.ecommerce.favorites.services.impl;

import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.favorites.controllers.dto.FavoriteResponse;
import com.challenge.ecommerce.favorites.controllers.dto.FavoriteShortResponse;
import com.challenge.ecommerce.favorites.controllers.dto.FavoriteUserResponse;
import com.challenge.ecommerce.favorites.mappers.IFavoriteMapper;
import com.challenge.ecommerce.favorites.models.FavoriteEntity;
import com.challenge.ecommerce.favorites.repositories.FavoriteRepository;
import com.challenge.ecommerce.favorites.services.IFavoriteService;
import com.challenge.ecommerce.products.repositories.ProductRepository;
import com.challenge.ecommerce.users.repositories.UserRepository;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.AuthUtils;
import com.challenge.ecommerce.utils.StringHelper;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FavoriteServiceImpl implements IFavoriteService {
  ProductRepository productRepository;
  UserRepository userRepository;
  FavoriteRepository favoriteRepository;
  IFavoriteMapper mapper;

  @Override
  public void addFavorite(String productId) {
    var product =
        productRepository
            .findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    if (!favoriteRepository.existsByProductIdAndUserAndDeletedAtIsNull(
        product.getId(), user.getId())) {
      FavoriteEntity favoriteEntity = new FavoriteEntity();
      favoriteEntity.setProduct(product);
      favoriteEntity.setUser(user);
      favoriteRepository.save(favoriteEntity);
    }
  }

  @Override
  public FavoriteShortResponse getFavoriteByProductIdUser(String productId) {
    var product =
        productRepository
            .findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    boolean favorite =
        favoriteRepository.existsByProductIdAndUserAndDeletedAtIsNull(
            product.getId(), user.getId());
    var resp = new FavoriteShortResponse();
    resp.setFavorite(favorite);
    return resp;
  }

  @Override
  public void removeFavorite(String productId) {
    var product =
        productRepository
            .findByIdAndDeletedAtIsNull(productId)
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.PRODUCT_NOT_FOUND));
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    var favorite =
        favoriteRepository.findByProductIdAndUserIdAndDeletedAtIsNull(
            product.getId(), user.getId());
    if (favorite.isPresent()) {
      favorite.get().setDeletedAt(LocalDateTime.now());
      favoriteRepository.save(favorite.get());
    }
  }

  @Override
  public ApiResponse<List<FavoriteUserResponse>> getAllFavoriteByUser(Pageable pageable) {
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    var favorites = favoriteRepository.findByUserIdAndDeletedAtIsNull(user.getId(), pageable);
    List<FavoriteUserResponse> responses =
        favorites.stream().map(mapper::favoriteEntityToDto).toList();
    return ApiResponse.<List<FavoriteUserResponse>>builder()
        .message("Get favorite successfully")
        .result(responses)
        .totalPages(favorites.getTotalPages())
        .total(favorites.getTotalElements())
        .page(pageable.getPageNumber() + 1)
        .limit(favorites.getNumberOfElements())
        .build();
  }

  @Override
  public ApiResponse<List<FavoriteResponse>> getAllFavorite(
      Pageable pageable, String userName, String productName) {
    var favorites =
        favoriteRepository.findAll(
            ((root, query, criteriaBuilder) -> {
              var predicates = new ArrayList<>();

              // Check nullable deletedAt
              predicates.add(criteriaBuilder.isNull(root.get("deletedAt")));

              // Filter by productName if provided
              if (productName != null && !productName.trim().isEmpty()) {
                var productSearch = "%" + StringHelper.toSlug(productName) + "%";
                var productJoin = root.join("product");
                predicates.add(criteriaBuilder.like(productJoin.get("slug"), productSearch));
              }

              // Filter by userName if provided
              if (userName != null && !userName.trim().isEmpty()) {
                var userSearch = "%" + (userName) + "%";
                var userJoin = root.join("user");
                predicates.add(criteriaBuilder.like(userJoin.get("name"), userSearch));
              }

              // Combine all predicates with AND
              query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
              return query.getRestriction();
            }),
            pageable);
    List<FavoriteResponse> responses =
        favorites.stream().map(mapper::favoriteDtoToFavoriteResponse).toList();
    return ApiResponse.<List<FavoriteResponse>>builder()
        .message("Get favorite successfully")
        .result(responses)
        .totalPages(favorites.getTotalPages())
        .total(favorites.getTotalElements())
        .page(pageable.getPageNumber() + 1)
        .limit(favorites.getNumberOfElements())
        .build();
  }
}
