package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.dto.auth.*;

public interface AuthService {
    void registration(RegistrationRequest registrationRequest);

    JwtResponse login(LoginRequest loginRequest);

    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void logout(LogoutRequest logoutRequest);
}
