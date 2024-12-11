package com.challenge.ecommerce.favorites.mappers;

import com.challenge.ecommerce.favorites.controllers.dto.FavoriteResponse;
import com.challenge.ecommerce.favorites.controllers.dto.FavoriteUserResponse;
import com.challenge.ecommerce.favorites.models.FavoriteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IFavoriteMapper {
  FavoriteUserResponse favoriteEntityToDto(FavoriteEntity favoriteEntity);

  @Mapping(target = "user", source = "user")
  FavoriteResponse favoriteDtoToFavoriteResponse(FavoriteEntity favoriteEntity);
}
