package ru.yakovlev05.school.flash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import ru.yakovlev05.school.flash.dto.message.MessageResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.service.MessageService;

import java.util.List;

@Tag(name = "Сообщения", description = "API работы с сообщениями")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Получить список сообщений в чате")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/chat/{chatId}")
    public List<MessageResponse> getListMessages(
            @CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
            @PathVariable Long chatId,
            @Min(0) @RequestParam(defaultValue = "0") Integer page,
            @Min(1) @RequestParam(defaultValue = "20") Integer limit) {
        return messageService.getListMessages(jwtAuthentication, chatId, page, limit);
    }

    @Operation(summary = "Удалить сообщение")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyMessage(@CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
                                @PathVariable Long messageId) {
        messageService.deleteMyMessage(jwtAuthentication, messageId);
    }
}
