package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yakovlev05.school.flash.repository.ChatParticipantRepository;
import ru.yakovlev05.school.flash.service.ChatParticipantService;

@RequiredArgsConstructor
@Service
public class ChatParticipantServiceImpl implements ChatParticipantService {

    private final ChatParticipantRepository chatParticipantRepository;

    @Transactional(readOnly = true)
    @Override
    public boolean isUserParticipant(Long userId, Long chatId) {
        return chatParticipantRepository.isChatParticipant(chatId, userId);
    }

    @Transactional
    @Override
    public void deleteParticipantsByChatId(Long chatId) {
        chatParticipantRepository.removeById_Chat_Id(chatId);
    }
}
