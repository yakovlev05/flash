package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.dto.message.MessageResponse;
import ru.yakovlev05.school.flash.dto.user.UserResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.Message;
import ru.yakovlev05.school.flash.exception.handler.ForbiddenException;
import ru.yakovlev05.school.flash.repository.MessageRepository;
import ru.yakovlev05.school.flash.service.ChatParticipantService;
import ru.yakovlev05.school.flash.service.MessageService;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    private final ChatParticipantService chatParticipantService;

    @Override
    public List<MessageResponse> getListMessages(JwtAuthentication jwtAuthentication, Long chatId) {
        if (!chatParticipantService.isUserParticipant(chatId, jwtAuthentication.getUserId())) {
            throw new ForbiddenException("Вы не являетесь участником этого чата");
        }

        return messageRepository.findAllByChatId(chatId).stream()
                .sorted(Comparator.comparingLong(Message::getId))
                .map(this::toDto)
                .toList();
    }

    @Override
    public void save(Message messageEntity) {
        messageRepository.save(messageEntity);
    }

    private MessageResponse toDto(Message message) {
        UserResponse userResponse = new UserResponse(message.getSender().getUsername());
        return new MessageResponse(
                message.getId(),
                message.getChat().getId(),
                userResponse,
                message.getText(),
                message.getSentAt()
        );
    }
}
