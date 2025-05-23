package ru.yakovlev05.school.flash.service;

public interface NotificationSenderService {
    void send (String text, String subject, String email);
}
