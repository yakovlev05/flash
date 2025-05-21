package ru.yakovlev05.school.flash.repository;

import org.springframework.data.repository.CrudRepository;
import ru.yakovlev05.school.flash.entity.Ticket;

import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Optional<Ticket> findByTicket(String ticket);
}
