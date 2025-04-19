package ru.yakovlev05.school.flash.dto;

import java.time.Instant;

public record MessageResponse(
        Long id,
        Long chatId,
        UserResponse user,
        String text,
        Instant sentAt
) {
}
