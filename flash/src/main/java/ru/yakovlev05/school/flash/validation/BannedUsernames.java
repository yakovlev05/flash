package ru.yakovlev05.school.flash.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BannedUsernamesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BannedUsernames {
    String message() default "это имя пользователя заблокировано для использования";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
