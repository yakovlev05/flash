package ru.yakovlev05.school.flash.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class BannedUsernamesValidator implements ConstraintValidator<BannedUsernames, String> {

    private static final Set<String> banned = Set.of(
            "admin",
            "moderator",
            "owner",
            "root"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !banned.contains(value.toLowerCase());
    }
}
