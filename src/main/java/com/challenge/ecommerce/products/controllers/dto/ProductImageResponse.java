package com.challenge.ecommerce.products.controllers.dto;

import com.challenge.ecommerce.utils.enums.TypeImage;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageResponse {
  String id;
  String images_url;
  TypeImage type_image;
}
