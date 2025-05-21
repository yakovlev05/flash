package ru.yakovlev05.school.flash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yakovlev05.school.flash.dto.auth.TicketResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.service.TicketService;

@SecurityRequirement(name = "JWT")
@Tag(name = "Билеты", description = "API работы с билетами")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Короткоживущий билет для подключения к ws")
    @PostMapping
    public TicketResponse createTicket(@CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication) {
        return ticketService.createTicket(jwtAuthentication);
    }

}
