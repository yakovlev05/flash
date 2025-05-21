package ru.yakovlev05.school.flash.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Запрос на создание группового чата")
public record CreateGroupChatRequest(
        @Schema(description = "Название чата", example = "Проектный практикум 2025")
        @NotBlank(message = "название чата не может быть пустым")
        String title,

        @Schema(description = "Список id участников чата", example = "[1, 2, 3]")
        @NotNull
        List<Long> participantsId
) {
}
