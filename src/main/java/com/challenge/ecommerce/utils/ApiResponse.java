package com.challenge.ecommerce.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  String message;
  Integer totalPages;
  T result;
  @PositiveOrZero Long total;
  @PositiveOrZero Integer page;

  @Min(1)
  Integer limit;
}
