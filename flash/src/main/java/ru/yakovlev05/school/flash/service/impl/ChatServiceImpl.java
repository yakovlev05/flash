package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yakovlev05.school.flash.dto.chat.ChatResponse;
import ru.yakovlev05.school.flash.dto.chat.CreateGroupChatRequest;
import ru.yakovlev05.school.flash.dto.chat.CreatePrivateChatRequest;
import ru.yakovlev05.school.flash.entity.Chat;
import ru.yakovlev05.school.flash.entity.ChatParticipant;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.User;
import ru.yakovlev05.school.flash.exception.ConflictException;
import ru.yakovlev05.school.flash.exception.ForbiddenException;
import ru.yakovlev05.school.flash.exception.NotFoundException;
import ru.yakovlev05.school.flash.mapper.ChatMapper;
import ru.yakovlev05.school.flash.repository.ChatRepository;
import ru.yakovlev05.school.flash.service.ChatParticipantService;
import ru.yakovlev05.school.flash.service.ChatService;
import ru.yakovlev05.school.flash.service.MessageService;
import ru.yakovlev05.school.flash.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    private final UserService userService;

    private final ChatMapper chatMapper;
    private final MessageService messageService;
    private final ChatParticipantService chatParticipantService;

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

    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public Chat getById(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Чат с messageId '%d' не найден", chatId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatResponse> getMyListChat(JwtAuthentication jwtAuthentication, Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id"));
        return chatRepository.findAllByUserId(jwtAuthentication.getUserId(), pageable).stream()
                .map(chatMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteMyChat(JwtAuthentication jwtAuthentication, Long chatId) {
        Chat chat = getByIdAndUserId(chatId, jwtAuthentication.getUserId());
        if (chat.getType().equals(Chat.Type.GROUP) && !isCreatorForGroupChat(chat, jwtAuthentication.getUserId())) {
            throw new ForbiddenException("Вы не создатель чата");
        }

        delete(chat);
    }

    private void save(Chat chat) {
        chatRepository.save(chat);
    }

    private void delete(Chat chat) {
        messageService.deleteMessagesByChatId(chat.getId());
        chatParticipantService.deleteParticipantsByChatId(chat.getId());
        chatRepository.delete(chat);
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

    private Chat getByIdAndUserId(Long chatId, Long userId) {
        return chatRepository.findByChatIdAndUserId(chatId, userId)
                .orElseThrow(() -> new NotFoundException("Чат с id '%d' не найден", chatId));
    }

    private boolean isCreatorForGroupChat(Chat chat, Long userId) {
        return chat.getParticipants().stream()
                .anyMatch(participant -> participant.getId().getUser().getId().equals(userId));
    }
}
