package ru.yakovlev05.school.flash.websocket.session;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionStorage {

    // chatId-->sessions
    private final Map<Long, Set<WebSocketSession>> sessionStorage = new ConcurrentHashMap<>();

    public void addSession(Long chatId, WebSocketSession session) {
        if (sessionStorage.containsKey(chatId)) {
            sessionStorage.get(chatId).add(session);
        } else {
            Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
            sessions.add(session);

            sessionStorage.put(chatId, sessions);
        }
    }

    public void removeSession(Long chatId, WebSocketSession session) {
        if (!sessionStorage.containsKey(chatId)) {
            return;
        }

        sessionStorage.get(chatId).remove(session);
    }

    public Set<WebSocketSession> getAllSessions(Long chatId) {
        if (!sessionStorage.containsKey(chatId)) {
            return new HashSet<>();
        }

        return sessionStorage.get(chatId);
    }
}
