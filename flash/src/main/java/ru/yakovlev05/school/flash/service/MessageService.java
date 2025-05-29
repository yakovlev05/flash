package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.dto.message.MessageResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.Message;

import java.util.List;

public interface MessageService {
    List<MessageResponse> getListMessages(JwtAuthentication jwtAuthentication, Long chatId, Integer page, Integer limit);

    void save(Message messageEntity);

    void deleteMyMessage(JwtAuthentication jwtAuthentication, Long messageId);

    void deleteMessagesByChatId(Long chatId);

    List<MessageResponse> getListMessages(Long chatId, Integer page, Integer limit);

    void deleteMessageById(Long messageId);
}
