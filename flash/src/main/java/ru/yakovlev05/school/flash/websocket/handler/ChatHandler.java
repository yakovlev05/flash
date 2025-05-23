package ru.yakovlev05.school.flash.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.yakovlev05.school.flash.dto.message.SendMessageRequest;
import ru.yakovlev05.school.flash.dto.message.SendMessageResponse;
import ru.yakovlev05.school.flash.dto.user.UserResponse;
import ru.yakovlev05.school.flash.entity.Message;
import ru.yakovlev05.school.flash.eventlistener.event.MessageCreatedEvent;
import ru.yakovlev05.school.flash.service.ChatService;
import ru.yakovlev05.school.flash.service.MessageService;
import ru.yakovlev05.school.flash.service.UserService;
import ru.yakovlev05.school.flash.websocket.session.SessionStorage;

import java.io.IOException;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatHandler extends TextWebSocketHandler {

    private final SessionStorage sessionStorage;

    private final MessageService messageService;
    private final ChatService chatService;
    private final UserService userService;

    private final ObjectMapper objectMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionStorage.addSession((Long) session.getAttributes().get("chatId"), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionStorage.removeSession((Long) session.getAttributes().get("chatId"), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long chatId = (Long) session.getAttributes().get("chatId");
        Long userId = (Long) session.getAttributes().get("userId");

        SendMessageRequest messageRequest = objectMapper.readValue(message.getPayload(), SendMessageRequest.class);

        Message messageEntity = new Message();
        messageEntity.setChat(chatService.getById(chatId));
        messageEntity.setSender(userService.getById(userId));
        messageEntity.setText(messageRequest.text());

        messageService.save(messageEntity);

        applicationEventPublisher.publishEvent(
                new MessageCreatedEvent(
                        messageEntity.getId(),
                        userId,
                        messageEntity.getText()
                )
        );

        sendMessages(toDto(messageEntity), session, chatId);
    }

    private SendMessageResponse toDto(Message message) {
        return new SendMessageResponse(
                message.getId(),
                new UserResponse(message.getSender().getUsername(), message.getSender().getEmail()),
                message.getText(),
                message.getSentAt()
        );
    }

    private void sendMessages(SendMessageResponse response, WebSocketSession currentSession, Long chatId) throws IOException {
        Set<WebSocketSession> sessions = sessionStorage.getAllSessions(chatId);

        String stringResponse = objectMapper.writeValueAsString(response);
        WebSocketMessage<String> message = new TextMessage(stringResponse);

        sessions.stream()
                .filter(session -> !session.getId().equals(currentSession.getId()))
                .forEach(session -> {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        log.info("Unable to send message, session messageId: {}, chatId: {}", session.getId(), chatId);
                        sessions.remove(session);
                    }
                });
    }
}
