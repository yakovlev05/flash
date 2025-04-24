package ru.yakovlev05.school.flash.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import ru.yakovlev05.school.flash.dto.auth.*;

public interface AuthService {
    void registration(RegistrationRequest registrationRequest);

    ResponseEntity<Void> login(LoginRequest loginRequest);

    void logout(LogoutRequest logoutRequest);

    String refreshToken(String validatedRefreshToken, HttpServletResponse response);
}
