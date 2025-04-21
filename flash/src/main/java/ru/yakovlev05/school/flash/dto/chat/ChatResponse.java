package ru.yakovlev05.school.flash.dto.chat;

import ru.yakovlev05.school.flash.dto.user.UserResponse;
import ru.yakovlev05.school.flash.entity.Chat;

import java.util.List;

public record ChatResponse(Long id, String title, Chat.Type type, List<UserResponse> participants) {
}
