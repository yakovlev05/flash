package ru.yakovlev05.school.flash.dto.chat;

import jakarta.validation.constraints.NotNull;

public record CreatePrivateChatRequest(
        @NotNull
        Long userId
) {
}
