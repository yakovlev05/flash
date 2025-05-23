package ru.yakovlev05.school.flash.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.dto.chat.ChatResponse;
import ru.yakovlev05.school.flash.dto.chat.CreateGroupChatRequest;
import ru.yakovlev05.school.flash.dto.chat.CreatePrivateChatRequest;
import ru.yakovlev05.school.flash.entity.Chat;
import ru.yakovlev05.school.flash.entity.ChatParticipant;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.User;
import ru.yakovlev05.school.flash.exception.ConflictException;
import ru.yakovlev05.school.flash.exception.NotFoundException;
import ru.yakovlev05.school.flash.mapper.ChatMapper;
import ru.yakovlev05.school.flash.repository.ChatRepository;
import ru.yakovlev05.school.flash.service.ChatService;
import ru.yakovlev05.school.flash.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    private final UserService userService;

    private final ChatMapper chatMapper;

    @Transactional
    @Override
    public ChatResponse createPrivateChat(JwtAuthentication jwtAuthentication, CreatePrivateChatRequest createPrivateChatRequest) {
        if (jwtAuthentication.getUserId().equals(createPrivateChatRequest.userId())) {
            throw new ConflictException("Нельзя создать чат с самим собой");
        }

        Chat existsChat = getPrivateChatOrNull(jwtAuthentication.getUserId(), createPrivateChatRequest.userId());
        if (existsChat != null) {
            return chatMapper.toDto(existsChat);
        }

        User sender = userService.getById(jwtAuthentication.getUserId());
        User recipient = userService.getById(createPrivateChatRequest.userId());

        Chat chat = new Chat();
        chat.setType(Chat.Type.PRIVATE);

        ChatParticipant senderParticipant = createChatParticipant(chat, sender);
        ChatParticipant recipientParticipant = createChatParticipant(chat, recipient);

        chat.setParticipants(List.of(senderParticipant, recipientParticipant));
        save(chat);

        return chatMapper.toDto(chat);
    }

    @Override
    public ChatResponse createGroupChat(JwtAuthentication jwtAuthentication, CreateGroupChatRequest createGroupChatRequest) {
        if (createGroupChatRequest.participantsId().contains(jwtAuthentication.getUserId())) {
            throw new ConflictException("Id создателя не нужно передавать в списке участников");
        }
        // Если есть невалидные messageId, то они будут проигнорированы
        List<User> participants = userService.getListUsersByIds(createGroupChatRequest.participantsId());

        Chat chat = new Chat();
        chat.setType(Chat.Type.GROUP);
        chat.setTitle(createGroupChatRequest.title());
        chat.setParticipants(
                participants.stream()
                        .map(p -> createChatParticipant(chat, p))
                        .collect(Collectors.toList())
        );

        ChatParticipant admin = createChatParticipant(chat, userService.getById(jwtAuthentication.getUserId()));
        admin.setRole(ChatParticipant.Role.ADMIN);
        chat.getParticipants().add(admin);

        save(chat);
        return chatMapper.toDto(chat);
    }

    @Override
    public Chat getById(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Чат с messageId '%d' не найден", chatId));
    }

    private void save(Chat chat) {
        chatRepository.save(chat);
    }

    private Chat getPrivateChatOrNull(Long firstUserId, Long secondUserId) {
        return chatRepository.findPrivateChatByParticipantsId(firstUserId, secondUserId)
                .stream().findFirst().orElse(null);
    }

    private ChatParticipant createChatParticipant(Chat chat, User user) {
        ChatParticipant chatParticipant = new ChatParticipant();
        chatParticipant.setId(new ChatParticipant.CompositeId(chat, user));
        return chatParticipant;
    }
}
