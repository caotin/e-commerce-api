package com.challenge.ecommerce.utils.components.customannotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class NotBlankIdsValidator implements ConstraintValidator<NotBlankIds, List<String>> {

  @Override
  public boolean isValid(
      List<String> strings, ConstraintValidatorContext constraintValidatorContext) {
    if (strings == null || strings.isEmpty()) {
      return false;
    }
    for (String string : strings) {
      if (string == null || string.trim().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(NotBlankIds constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
}
