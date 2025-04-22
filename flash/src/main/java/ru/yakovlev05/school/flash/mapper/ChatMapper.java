package ru.yakovlev05.school.flash.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yakovlev05.school.flash.dto.chat.ChatResponse;
import ru.yakovlev05.school.flash.entity.Chat;

@RequiredArgsConstructor
@Component
public class ChatMapper {

    private final ChatParticipantMapper chatParticipantMapper;

    public ChatResponse toDto(Chat chat) {
        return new ChatResponse(
                chat.getId(),
                chat.getTitle(),
                chat.getType(),
                chat.getParticipants().stream()
                        .map(chatParticipantMapper::toDto)
                        .toList()
        );
    }

}
