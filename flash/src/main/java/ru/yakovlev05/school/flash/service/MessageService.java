package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.dto.MessageResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.Message;

import java.util.List;

public interface MessageService {
    List<MessageResponse> getListMessages(JwtAuthentication jwtAuthentication, Long chatId);

    void save(Message messageEntity);
}
