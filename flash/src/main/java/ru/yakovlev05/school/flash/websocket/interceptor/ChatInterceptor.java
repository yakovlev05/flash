package ru.yakovlev05.school.flash.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.yakovlev05.school.flash.service.ChatParticipantService;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class ChatInterceptor implements HandshakeInterceptor {

    private final ChatParticipantService chatParticipantService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        Long chatId = null;
        try {
            chatId = Long.parseLong(resolveChatId(request));
        } catch (NumberFormatException ignored) {
        }

        if (chatId == null) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }

        if (!chatParticipantService.isUserParticipant((Long) attributes.get("userId"), chatId)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }

        attributes.put("chatId", chatId);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    // Повторяющийся код
    private String resolveChatId(ServerHttpRequest request) {
        String[] queryPairs = request.getURI().getQuery().split("&");

        for (String queryPair : queryPairs) {
            String[] pair = queryPair.split("=");

            if (pair.length == 2 && pair[0].equals("chatId")) {
                return pair[1];
            }
        }

        return null;
    }
}
