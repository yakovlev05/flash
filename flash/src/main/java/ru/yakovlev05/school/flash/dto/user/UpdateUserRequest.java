package ru.yakovlev05.school.flash.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 5, message = "длина имени пользователя должна быть не менее 5")
        String username,

        @Pattern(regexp = "`|~|@|\"|#|№|\\$|;|%|^|:|&|\\?|\\*|\\(|\\)|>|<|/|\\+|=|-|\\{|}|\\[|]",
                message = "пароль должен содержать специальные символы")
        @Size(min = 8, message = "длина пароля должны быть не менее 8 символов")
        String password
) {
}
