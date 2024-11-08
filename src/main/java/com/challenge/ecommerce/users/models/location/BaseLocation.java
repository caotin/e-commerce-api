package com.challenge.ecommerce.users.models.location;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class BaseLocation {
  @Id int code;
  String name;
}
