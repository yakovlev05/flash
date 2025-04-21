package ru.yakovlev05.school.flash.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateGroupChatRequest(
        @NotBlank(message = "название чата не может быть пустым")
        String title,

        @NotNull
        List<Long> participantsId
) {
}
