package ru.yakovlev05.school.flash.service;

public interface ChatParticipantService {
    boolean isUserParticipant(Long userId, Long chatId);

    void deleteParticipantsByChatId(Long chatId);
}
