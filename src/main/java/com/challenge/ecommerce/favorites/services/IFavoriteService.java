package com.challenge.ecommerce.favorites.services;

import com.challenge.ecommerce.favorites.controllers.dto.FavoriteShortResponse;
import com.challenge.ecommerce.utils.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface IFavoriteService {
  void addFavorite(String productId);

  FavoriteShortResponse getFavoriteByProductIdUser(String productId);

  void removeFavorite(String productId);

  ApiResponse<?> getAllFavoriteByUser(Pageable pageable);

  ApiResponse<?> getAllFavorite(Pageable pageable, String userName, String productName);
}
