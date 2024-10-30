package com.challenge.ecommerce.utils.components.customannotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AllowedImageFileTypeValidator
    implements ConstraintValidator<AllowedImageFileType, String> {

  private String[] allowedExtensions;

  @Override
  public boolean isValid(String fileName, ConstraintValidatorContext constraintValidatorContext) {

    if (fileName == null) {
      return true;
    }

    return Arrays.stream(allowedExtensions).anyMatch(fileName::endsWith);
  }

  @Override
  public void initialize(AllowedImageFileType constraintAnnotation) {
    this.allowedExtensions = constraintAnnotation.allowedExtensions();
  }
}
