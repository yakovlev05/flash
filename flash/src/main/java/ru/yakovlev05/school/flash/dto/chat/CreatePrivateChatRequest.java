package ru.yakovlev05.school.flash.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Создать приватный (личный) чат")
public record CreatePrivateChatRequest(

        @Schema(description = "messageId собеседника", example = "1")
        @NotNull
        Long userId
) {
}
