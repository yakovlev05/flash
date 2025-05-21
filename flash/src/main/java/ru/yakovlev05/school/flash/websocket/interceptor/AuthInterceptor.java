package ru.yakovlev05.school.flash.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.yakovlev05.school.flash.entity.Ticket;
import ru.yakovlev05.school.flash.service.TicketService;
import ru.yakovlev05.school.flash.service.UserService;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandshakeInterceptor {

    private final TicketService ticketService;
    private final UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        String ticket = resolveTicket(request);
        if (ticket == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        Ticket ticketEntity = ticketService.findByTicket(ticket);
        if (ticketEntity == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        if (!userService.existsByUserId(ticketEntity.getUserId())) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        attributes.put("userId", ticketEntity.getUserId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    private String resolveTicket(ServerHttpRequest request) {
        String[] queryPairs = request.getURI().getQuery().split("&");

        for (String queryPair : queryPairs) {
            String[] pair = queryPair.split("=");

            if (pair.length == 2 && pair[0].equals("ticket")) {
                return pair[1];
            }
        }

        return null;
    }
}
