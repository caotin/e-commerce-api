package com.challenge.ecommerce.utils.components.customannotation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedImageFileTypeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedImageFileType {
    String message() default "Invalid file extension";

    String[] allowedExtensions() default { ".jpg", ".png", ".tiff", ".webp", ".jfif" };

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
