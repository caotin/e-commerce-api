package com.challenge.ecommerce.products.controllers.dto;

import com.challenge.ecommerce.utils.enums.TypeImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageCreateDto {
  @NotBlank(message = "Image URL cannot be blank")
  String images_url;

  @NotNull(message = "Image type is required")
  TypeImage type_image;
}
