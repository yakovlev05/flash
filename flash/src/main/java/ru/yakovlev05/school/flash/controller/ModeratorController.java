package ru.yakovlev05.school.flash.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yakovlev05.school.flash.dto.chat.ChatResponse;
import ru.yakovlev05.school.flash.dto.message.MessageResponse;
import ru.yakovlev05.school.flash.service.ChatService;
import ru.yakovlev05.school.flash.service.MessageService;

import java.util.List;

@Validated
@Tag(name = "Модерация", description = "API для модератора")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/moderator")
public class ModeratorController {

    private final MessageService messageService;
    private final ChatService chatService;

    @GetMapping("/chat/{chatId}/messages")
    public List<MessageResponse> getAllMessages(@PathVariable Long chatId,
                                                @Min(0) @RequestParam(defaultValue = "0") Integer page,
                                                @Min(1) @RequestParam(defaultValue = "20") Integer limit) {
        return messageService.getListMessages(chatId, page, limit);
    }

    @DeleteMapping("/message/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessageById(messageId);
    }

    @GetMapping("/chats")
    public List<ChatResponse> getAllChats(@Min(0) @RequestParam(defaultValue = "0") Integer page,
                                          @Min(1) @RequestParam(defaultValue = "20") Integer limit) {
        return chatService.getListChats(page, limit);
    }

    @DeleteMapping("/chats/{chatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChat(@PathVariable Long chatId) {
        chatService.deleteChatById(chatId);
    }
}
