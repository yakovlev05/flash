package ru.yakovlev05.school.flash.dto.message;

import ru.yakovlev05.school.flash.dto.UserResponse;

import java.time.Instant;

public record SendMessageResponse(Long id, UserResponse sender, String text, Instant sendAt) {
}
