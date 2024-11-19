package com.challenge.ecommerce.reviews.mappers;

import com.challenge.ecommerce.reviews.controllers.dtos.ReviewCreateRequest;
import com.challenge.ecommerce.reviews.controllers.dtos.ReviewUpdateRequest;
import com.challenge.ecommerce.reviews.models.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IReviewMapper {
  ReviewEntity dtoCreateToEntity(ReviewCreateRequest request);

  ReviewEntity dtoUpdateToEntity(ReviewUpdateRequest request, @MappingTarget ReviewEntity entity);
}
