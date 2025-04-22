package ru.yakovlev05.school.flash.mapper;

import org.springframework.stereotype.Component;
import ru.yakovlev05.school.flash.dto.user.UserResponse;
import ru.yakovlev05.school.flash.entity.ChatParticipant;

@Component
public class ChatParticipantMapper {

    public UserResponse toDto(ChatParticipant chatParticipant) {
        return new UserResponse(chatParticipant.getId().getUser().getUsername());
    }

}
