package ru.yakovlev05.school.flash.kafka.event;

public record NotificationEvent(
        String text,
        String title,
        String destination,
        NotificationDirection direction
) {
}
