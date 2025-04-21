package ru.yakovlev05.school.flash.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yakovlev05.school.flash.dto.chat.ChatResponse;
import ru.yakovlev05.school.flash.dto.chat.CreateGroupChatRequest;
import ru.yakovlev05.school.flash.dto.chat.CreatePrivateChatRequest;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.service.ChatService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @SecurityRequirement(name = "JWT")
    @PostMapping("/private")
    public ChatResponse createPrivateChat(
            @CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
            @Valid @RequestBody CreatePrivateChatRequest createPrivateChatRequest) {
        return chatService.createPrivateChat(jwtAuthentication, createPrivateChatRequest);
    }

    @SecurityRequirement(name = "JWT")
    @PostMapping("/group")
    public ChatResponse createGroupChat(
            @CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
            @Valid @RequestBody CreateGroupChatRequest createGroupChatRequest) {
        return chatService.createGroupChat(jwtAuthentication, createGroupChatRequest);
    }

}
