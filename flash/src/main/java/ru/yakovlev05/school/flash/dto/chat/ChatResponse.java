package ru.yakovlev05.school.flash.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.yakovlev05.school.flash.dto.user.UserResponse;
import ru.yakovlev05.school.flash.entity.Chat;

import java.util.List;

@Schema(description = "Информация о чате")
public record ChatResponse(
        @Schema(description = "id чата", example = "1")
        Long id,

        @Schema(description = "Название чата", example = "Андрей")
        String title,

        @Schema(description = "Тип чата", example = "PRIVATE")
        Chat.Type type,

        @Schema(description = "Участники чата")
        List<UserResponse> participants
) {
}
