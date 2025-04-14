package ru.yakovlev05.school.flash.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.util.JwtUtil;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        String token = resolveToken(request);
        if (token == null || !jwtUtil.validateAccessToken(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        JwtAuthentication authentication = (JwtAuthentication) jwtUtil.getAuthentication(token);
        attributes.put("userId", authentication.getUserId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    private String resolveToken(ServerHttpRequest request) {
        String[] queryPairs = request.getURI().getQuery().split("&");

        for (String queryPair : queryPairs) {
            String[] pair = queryPair.split("=");

            if (pair.length == 2 && pair[0].equals("token")) {
                return pair[1];
            }
        }

        return null;
    }
}
