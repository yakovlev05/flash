package ru.yakovlev05.school.flash.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на аутентификацию (вход)")
public record LoginRequest(
        @Schema(description = "Имя пользователя", example = "andrey")
        String username,

        @Schema(description = "Пароль", example = "#MyPassword")
        String password
) {
}
