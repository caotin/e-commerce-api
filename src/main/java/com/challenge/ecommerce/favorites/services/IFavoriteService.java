package com.challenge.ecommerce.favorites.services;

import com.challenge.ecommerce.favorites.controllers.dto.FavoriteResponse;
import com.challenge.ecommerce.favorites.controllers.dto.FavoriteShortResponse;
import com.challenge.ecommerce.favorites.controllers.dto.FavoriteUserResponse;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFavoriteService {
  void addFavorite(String productId);

  FavoriteShortResponse getFavoriteByProductIdUser(String productId);

  void removeFavorite(String productId);

  ApiResponse<List<FavoriteUserResponse>> getAllFavoriteByUser(Pageable pageable);

  ApiResponse<List<FavoriteResponse>> getAllFavorite(
      Pageable pageable, String userName, String productName);
}
