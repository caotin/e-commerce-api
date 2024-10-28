package com.challenge.ecommerce.utils.componets.customannotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class NotBlankIdsValidator implements ConstraintValidator<NotBlankIds, List<String>> {
  @Override
  public void initialize(NotBlankIds constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(List<String> ids, ConstraintValidatorContext constraintValidatorContext) {
    if (ids == null || ids.isEmpty()) {
      return false;
    }
    for (String id : ids) {
      if (id == null || id.trim().isEmpty()) {
        return false;
      }
    }

    return true;
  }
}
