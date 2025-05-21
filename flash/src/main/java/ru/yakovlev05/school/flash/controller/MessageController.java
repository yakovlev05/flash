package ru.yakovlev05.school.flash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @Operation(summary = "Получить список сообщение в чате")
    @GetMapping("/chat/{id}")
    public List<MessageResponse> getListMessages(
            @CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
            @CurrentSecurityContext @PathVariable Long id) {
        return messageService.getListMessages(jwtAuthentication, id);
    }
}
