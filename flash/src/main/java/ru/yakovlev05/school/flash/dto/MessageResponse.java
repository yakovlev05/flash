package ru.yakovlev05.school.flash.dto;

public record MessageResponse(
        Long id,
        Long chatId,
        UserResponse user,
        String text,
        Long sentAt
) {
}
