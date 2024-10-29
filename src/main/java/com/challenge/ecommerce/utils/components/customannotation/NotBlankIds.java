package com.challenge.ecommerce.utils.components.customannotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotBlankIdsValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankIds {
  String message() default "List of Ids cannot be empty or contain blank values";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
