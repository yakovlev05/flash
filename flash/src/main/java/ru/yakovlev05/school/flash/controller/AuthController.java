package ru.yakovlev05.school.flash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yakovlev05.school.flash.dto.auth.LoginRequest;
import ru.yakovlev05.school.flash.dto.auth.LogoutRequest;
import ru.yakovlev05.school.flash.dto.auth.RegistrationRequest;
import ru.yakovlev05.school.flash.service.AuthService;

@Tag(name = "Аутентификация", description = "API регистрация, аутентификации, управления сессиями")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Создать аккаунт")
    @PostMapping("/registration")
    public void registration(@Valid @RequestBody RegistrationRequest registrationRequest) {
        authService.registration(registrationRequest);
    }

    @Operation(summary = "Войти в аккаунт")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(summary = "Удалить текущую сессию")
    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);
    }
}
