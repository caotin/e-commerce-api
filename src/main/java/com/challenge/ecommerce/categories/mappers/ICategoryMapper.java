package com.challenge.ecommerce.categories.mappers;

import com.challenge.ecommerce.categories.controllers.dto.CategoryCreateDto;
import com.challenge.ecommerce.categories.controllers.dto.CategoryParentResponse;
import com.challenge.ecommerce.categories.controllers.dto.CategoryResponse;
import com.challenge.ecommerce.categories.controllers.dto.CategoryUpdateDto;
import com.challenge.ecommerce.categories.models.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {
  CategoryEntity categoryCreateDtoToEntity(CategoryCreateDto request);

  CategoryResponse categoryEntityToDto(CategoryEntity entity);

  CategoryParentResponse categoryEntityToParentDto(CategoryEntity entity);

  CategoryEntity updateCategoryFromDto(
      CategoryUpdateDto request, @MappingTarget CategoryEntity oldEntity);
}
