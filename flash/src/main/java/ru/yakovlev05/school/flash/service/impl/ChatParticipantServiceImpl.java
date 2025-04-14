package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.repository.ChatParticipantRepository;
import ru.yakovlev05.school.flash.service.ChatParticipantService;

@RequiredArgsConstructor
@Service
public class ChatParticipantServiceImpl implements ChatParticipantService {

    private final ChatParticipantRepository chatParticipantRepository;

    @Override
    public boolean isUserParticipant(Long userId, Long chatId) {
        return chatParticipantRepository.existsByChatIdAndUserId(chatId, userId);
    }
}
