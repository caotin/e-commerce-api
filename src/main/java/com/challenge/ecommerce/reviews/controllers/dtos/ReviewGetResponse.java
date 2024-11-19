package com.challenge.ecommerce.reviews.controllers.dtos;

import com.challenge.ecommerce.users.controllers.dtos.UserGetResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewGetResponse {
  String id;
  String content;
  String rating;
  UserGetResponse user;
  LocalDateTime updatedAt;
}
