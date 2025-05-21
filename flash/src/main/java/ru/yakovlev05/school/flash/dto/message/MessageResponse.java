package ru.yakovlev05.school.flash.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.yakovlev05.school.flash.dto.user.UserResponse;

import java.time.Instant;

@Schema(description = "Информация о сообщении")
public record MessageResponse(
        @Schema(description = "id сообщения", examples = "1")
        Long id,

        @Schema(description = "id чата", example = "5")
        Long chatId,

        @Schema(description = "Информация об отправителе")
        UserResponse user,

        @Schema(description = "Текст сообщения", example = "Hello, world!")
        String text,

        @Schema(description = "Дата отправки")
        Instant sentAt
) {
}
