package ru.yakovlev05.school.flash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yakovlev05.school.flash.dto.chat.ChatResponse;
import ru.yakovlev05.school.flash.dto.chat.CreateGroupChatRequest;
import ru.yakovlev05.school.flash.dto.chat.CreatePrivateChatRequest;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.service.ChatService;

import java.util.List;

@Validated
@Tag(name = "Чаты", description = "API управления чатами")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "Создать приватный чат")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/private")
    public ChatResponse createPrivateChat(
            @CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
            @Valid @RequestBody CreatePrivateChatRequest createPrivateChatRequest) {
        return chatService.createPrivateChat(jwtAuthentication, createPrivateChatRequest);
    }

    @Operation(summary = "Создать групповой чат")
    @SecurityRequirement(name = "JWT")
    @PostMapping("/group")
    public ChatResponse createGroupChat(
            @CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
            @Valid @RequestBody CreateGroupChatRequest createGroupChatRequest) {
        return chatService.createGroupChat(jwtAuthentication, createGroupChatRequest);
    }

    @Operation(summary = "Получить список чатов пользователя")
    @SecurityRequirement(name = "JWT")
    @GetMapping
    public List<ChatResponse> getMyListChat(
            @CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
            @Min(0) @RequestParam(defaultValue = "0") Integer page,
            @Min(1) @RequestParam(defaultValue = "20") Integer limit) {
        return chatService.getMyListChat(jwtAuthentication, page, limit);
    }

    @Operation(summary = "Удалить чат")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{chatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyChat(@CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
                             @Positive @PathVariable Long chatId) {
        chatService.deleteMyChat(jwtAuthentication, chatId);
    }
}
