package com.challenge.ecommerce.favorites.controllers.dto;

import com.challenge.ecommerce.products.controllers.dto.ProductFavoriteResponse;
import com.challenge.ecommerce.users.controllers.dtos.UserGetResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteResponse {
  ProductFavoriteResponse product;
  UserGetResponse user;
}
