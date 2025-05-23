package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.kafka.event.NotificationEvent;

public interface NotificationService {
    void send (NotificationEvent event);
}
