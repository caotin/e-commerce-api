package com.challenge.ecommerce.favorites.controllers.dto;

import com.challenge.ecommerce.products.controllers.dto.ProductFavoriteResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteUserResponse {
  ProductFavoriteResponse product;
}
