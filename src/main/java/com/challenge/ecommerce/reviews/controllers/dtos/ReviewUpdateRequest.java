package com.challenge.ecommerce.reviews.controllers.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewUpdateRequest {
  @Size(max = 1000, message = "Content cannot exceed 1000 characters")
  String content;

  @Min(1)
  @Max(5)
  Integer rating;
}
