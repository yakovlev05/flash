package ru.yakovlev05.school.flash.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import ru.yakovlev05.school.flash.websocket.handler.ChatHandler;
import ru.yakovlev05.school.flash.websocket.interceptor.AuthInterceptor;
import ru.yakovlev05.school.flash.websocket.interceptor.ChatInterceptor;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final AuthInterceptor authInterceptor;
    private final ChatInterceptor chatInterceptor;

    private final ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/ws/chat")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .addInterceptors(authInterceptor) // Аутентификация
                .addInterceptors(chatInterceptor); // Извлечение нужных данных (chatId и т.п.) + проверка доступа к чату
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxSessionIdleTimeout(30 * 60 * 1000L); // Сколько сессия будет находиться в простое перед отключением
        return container;
    }

}
