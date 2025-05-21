package ru.yakovlev05.school.flash.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на выход, удаление сессии")
public record LogoutRequest(
        @Schema(description = "refresh токен", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ3ODM0NDMxLCJleHAiOjE3NDc4ODQ0MzF9.JHKaP1zdvc8S_u-hD1TxAJGqSlNw0gt4wBBttS2aPAA")
        String refreshToken
) {
}
