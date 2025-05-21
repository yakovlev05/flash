package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.dto.auth.TicketResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.Ticket;
import ru.yakovlev05.school.flash.props.TicketProps;
import ru.yakovlev05.school.flash.repository.TicketRepository;
import ru.yakovlev05.school.flash.service.TicketService;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketProps ticketProps;

    @Override
    public TicketResponse createTicket(JwtAuthentication jwtAuthentication) {
        Ticket ticket = new Ticket();
        ticket.setTicket(UUID.randomUUID().toString());
        ticket.setUserId(jwtAuthentication.getUserId());
        ticket.setTimeout(ticketProps.getTimeToLiveInSeconds());
        ticketRepository.save(ticket);

        return new TicketResponse(ticket.getTicket());
    }

    @Override
    public Ticket findByTicket(String ticket) {
        return ticketRepository.findByTicket(ticket).orElse(null);
    }
}
