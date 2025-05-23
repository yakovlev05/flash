package ru.yakovlev05.school.flash.eventlistener.event;

public record MessageCreatedEvent(
        Long messageId,
        Long senderId,
        String text
) {
}
