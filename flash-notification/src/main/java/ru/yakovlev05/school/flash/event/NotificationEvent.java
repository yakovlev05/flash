package ru.yakovlev05.school.flash.event;

public record NotificationEvent(
        String text,
        String title,
        String destination,
        NotificationDirection direction
) {
}
