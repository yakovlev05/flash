package ru.yakovlev05.school.flash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yakovlev05.school.flash.dto.MessageResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.service.MessageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/chat/{id}")
    public List<MessageResponse> getListMessages(
            @CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
            @CurrentSecurityContext @PathVariable Long id) {
        return messageService.getListMessages(jwtAuthentication, id);
    }
}
