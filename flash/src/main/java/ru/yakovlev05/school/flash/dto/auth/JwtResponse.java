package ru.yakovlev05.school.flash.dto.auth;

public record JwtResponse(String accessToken, String refreshToken, Long expiredAt) {
}
