package ru.yakovlev05.school.flash.dto.message;

import ru.yakovlev05.school.flash.dto.user.UserResponse;

import java.time.Instant;

public record MessageResponse(
        Long id,
        Long chatId,
        UserResponse user,
        String text,
        Instant sentAt
) {
}
