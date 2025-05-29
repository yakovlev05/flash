package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yakovlev05.school.flash.dto.message.MessageResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.Message;
import ru.yakovlev05.school.flash.exception.ForbiddenException;
import ru.yakovlev05.school.flash.exception.NotFoundException;
import ru.yakovlev05.school.flash.mapper.MessageMapper;
import ru.yakovlev05.school.flash.metric.CountByDate;
import ru.yakovlev05.school.flash.repository.MessageRepository;
import ru.yakovlev05.school.flash.service.ChatParticipantService;
import ru.yakovlev05.school.flash.service.MessageService;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    private final ChatParticipantService chatParticipantService;

    private final MessageMapper messageMapper;

    @Override
    public List<MessageResponse> getListMessages(JwtAuthentication jwtAuthentication, Long chatId, Integer page, Integer limit) {
        ensureUserHasAccessToChat(jwtAuthentication, chatId);

        Pageable pageable = PageRequest.of(page, limit, Sort.by("sentAt"));
        return messageRepository.findAllByChatId(chatId, pageable).stream()
                .sorted(Comparator.comparingLong(Message::getId))
                .map(messageMapper::toDto)
                .toList();
    }

    @CountByDate(name = "messages")
    @Override
    public void save(Message messageEntity) {
        messageRepository.save(messageEntity);
    }

    @Override
    public void deleteMyMessage(JwtAuthentication jwtAuthentication, Long messageId) {
        Message message = getMyMessageById(messageId, jwtAuthentication.getUserId());
        delete(message);
    }

    @Transactional
    @Override
    public void deleteMessagesByChatId(Long chatId) {
        messageRepository.removeAllByChatId(chatId);
    }

    private void ensureUserHasAccessToChat(JwtAuthentication jwtAuthentication, Long chatId) {
        if (!chatParticipantService.isUserParticipant(chatId, jwtAuthentication.getUserId())) {
            throw new ForbiddenException("Вы не являетесь участником этого чата");
        }
    }

    private Message getMyMessageById(Long messageId, Long userId) {
        return messageRepository.findByMessageIdAndUserId(messageId, userId)
                .orElseThrow(() -> new NotFoundException("Сообщение с id '%d' не найдено", messageId));
    }

    private void delete(Message message) {
        messageRepository.delete(message);
    }
}
