package ru.yakovlev05.school.flash.websocket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.yakovlev05.school.flash.websocket.session.SessionStorage;

import java.io.IOException;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatHandler extends TextWebSocketHandler {

    private final SessionStorage sessionStorage;

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
        Set<WebSocketSession> sessions = sessionStorage.getAllSessions(chatId);
        WebSocketMessage<String> messageToSend = new TextMessage(message.getPayload());

        for (WebSocketSession sendSession : sessions) {
            if (sendSession.getId().equals(session.getId())) { // В сессию, откуда сообщение пришло, не отправляем
                continue;
            }

            try {
                sendSession.sendMessage(messageToSend);
            } catch (IOException e) {
                log.warn("Unable to send message, session id: {}, chatId: {}", sendSession.getId(), chatId);
            }
        }

    }
}
