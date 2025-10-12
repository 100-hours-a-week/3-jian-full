package com.jian.community.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$")
public @interface NicknameFormat {
    String message() default ValidationMessage.INVALID_NICKNAME;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
