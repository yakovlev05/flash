package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.dto.auth.TicketResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.Ticket;

public interface TicketService {
    TicketResponse createTicket(JwtAuthentication jwtAuthentication);

    Ticket findByTicket(String ticket);
}
