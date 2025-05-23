package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.dto.message.MessageResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.Message;
import ru.yakovlev05.school.flash.exception.ForbiddenException;
import ru.yakovlev05.school.flash.mapper.MessageMapper;
import ru.yakovlev05.school.flash.metric.CountByDate;
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

    private final MessageMapper messageMapper;

    @Override
    public List<MessageResponse> getListMessages(JwtAuthentication jwtAuthentication, Long chatId) {
        if (!chatParticipantService.isUserParticipant(chatId, jwtAuthentication.getUserId())) {
            throw new ForbiddenException("Вы не являетесь участником этого чата");
        }

        return messageRepository.findAllByChatId(chatId).stream()
                .sorted(Comparator.comparingLong(Message::getId))
                .map(messageMapper::toDto)
                .toList();
    }

    @CountByDate(name = "messages")
    @Override
    public void save(Message messageEntity) {
        messageRepository.save(messageEntity);
    }
}
