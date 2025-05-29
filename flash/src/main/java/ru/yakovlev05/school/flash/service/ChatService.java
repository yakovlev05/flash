package ru.yakovlev05.school.flash.service;

import jakarta.validation.constraints.Positive;
import ru.yakovlev05.school.flash.dto.chat.ChatResponse;
import ru.yakovlev05.school.flash.dto.chat.CreateGroupChatRequest;
import ru.yakovlev05.school.flash.dto.chat.CreatePrivateChatRequest;
import ru.yakovlev05.school.flash.entity.Chat;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;

import java.util.List;

public interface ChatService {
    ChatResponse createPrivateChat(JwtAuthentication jwtAuthentication, CreatePrivateChatRequest createPrivateChatRequest);

    ChatResponse createGroupChat(JwtAuthentication jwtAuthentication, CreateGroupChatRequest createGroupChatRequest);

    Chat getById(Long chatId);

    List<ChatResponse> getMyListChat(JwtAuthentication jwtAuthentication, Integer page, Integer limit);

    void deleteMyChat(JwtAuthentication jwtAuthentication, Long chatId);
}
