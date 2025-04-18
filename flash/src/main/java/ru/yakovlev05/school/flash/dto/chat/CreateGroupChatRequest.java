package ru.yakovlev05.school.flash.dto.chat;

import java.util.List;

public record CreateGroupChatRequest(String title, List<Long> participantsId) {
}
