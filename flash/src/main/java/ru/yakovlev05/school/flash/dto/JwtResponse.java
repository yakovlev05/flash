package ru.yakovlev05.school.flash.dto;

public record JwtResponse(String accessToken, String refreshToken, Long expiredAt) {
}
