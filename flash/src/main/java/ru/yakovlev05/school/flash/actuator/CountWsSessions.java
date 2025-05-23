package ru.yakovlev05.school.flash.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.yakovlev05.school.flash.websocket.session.SessionStorage;

@RequiredArgsConstructor
@Component
@Endpoint(id = "countWsSessions")
public class CountWsSessions {

    private final SessionStorage sessionStorage;

    @ReadOperation(produces = MediaType.TEXT_PLAIN_VALUE)
    public String read() {
        return sessionStorage.countSessions().toString();
    }

}
