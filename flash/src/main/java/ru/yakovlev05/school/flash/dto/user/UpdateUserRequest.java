package ru.yakovlev05.school.flash.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Обновить информацию о пользователе")
public record UpdateUserRequest(
        @Schema(description = "Новое имя пользователя", example = "andrey")
        @Size(min = 4, message = "длина имени пользователя должна быть не менее 4")
        String username,

        @Schema(description = "Email пользователя", example = "andrey")
        @Email
        String email,

        @Schema(description = "Новый пароль", example = "#MyPassword")
        @Pattern(regexp = "`|~|@|\"|#|№|\\$|;|%|^|:|&|\\?|\\*|\\(|\\)|>|<|/|\\+|=|-|\\{|}|\\[|]",
                message = "пароль должен содержать специальные символы")
        @Size(min = 8, message = "длина пароля должны быть не менее 8 символов")
        String password
) {
}
