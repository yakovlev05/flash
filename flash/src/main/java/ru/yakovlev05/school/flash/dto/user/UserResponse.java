package ru.yakovlev05.school.flash.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о пользователе")
public record UserResponse(
        @Schema(description = "Id пользователя", example = "1")
        Long id,

        @Schema(description = "Имя пользователя", example = "andrey")
        String username,

        @Schema(description = "Email пользователя", example = "andrey")
        String email
){
}
