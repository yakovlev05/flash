package ru.yakovlev05.school.flash.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yakovlev05.school.flash.dto.message.MessageResponse;
import ru.yakovlev05.school.flash.entity.Message;

@RequiredArgsConstructor
@Component
public class MessageMapper {

    private final UserMapper userMapper;

    public MessageResponse toDto(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getChat().getId(),
                userMapper.toDto(message.getSender()),
                message.getText(),
                message.getSentAt()
        );
    }

}
