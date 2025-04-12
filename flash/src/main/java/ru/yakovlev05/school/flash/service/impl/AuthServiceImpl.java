package ru.yakovlev05.school.flash.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.dto.*;
import ru.yakovlev05.school.flash.entity.RefreshToken;
import ru.yakovlev05.school.flash.entity.User;
import ru.yakovlev05.school.flash.props.SecurityProps;
import ru.yakovlev05.school.flash.service.AuthService;
import ru.yakovlev05.school.flash.service.RefreshTokenService;
import ru.yakovlev05.school.flash.service.UserService;
import ru.yakovlev05.school.flash.util.JwtUtil;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SecurityProps securityProps;

    @Override
    public void registration(RegistrationRequest registrationRequest) {
        if (userService.existsByUsername(registrationRequest.username())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(registrationRequest.username());
        user.setPassword(passwordEncoder.encode(registrationRequest.password()));

        userService.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        User user = userService.getByUsername(loginRequest.username());
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        String refreshToken = jwtUtil.generateRefreshToken(user);
        Date refreshTokenExpiredAt = jwtUtil.getRefreshClaims(refreshToken).getExpiration();
        refreshTokenService.save(refreshToken, user, refreshTokenExpiredAt);

        return new JwtResponse(
                jwtUtil.generateAccessToken(user),
                refreshToken,
                refreshTokenExpiredAt.toInstant().getEpochSecond()
        );
    }

    @Transactional
    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        if (!jwtUtil.validateRefreshToken(refreshTokenRequest.refreshToken())) {
            throw new RuntimeException("Invalid refresh token");
        }

        RefreshToken refreshTokenEntity = refreshTokenService.getByToken(refreshTokenRequest.refreshToken());

        refreshTokenService.delete(refreshTokenEntity);

        String refreshToken = jwtUtil.generateRefreshToken(refreshTokenEntity.getUser());
        Date refreshTokenExpiredAt = jwtUtil.getRefreshClaims(refreshToken).getExpiration();
        refreshTokenService.save(refreshToken, refreshTokenEntity.getUser(), refreshTokenExpiredAt);


        return new JwtResponse(
                jwtUtil.generateAccessToken(refreshTokenEntity.getUser()),
                refreshToken,
                refreshTokenExpiredAt.toInstant().getEpochSecond()
        );
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        RefreshToken refreshToken = refreshTokenService.getByToken(logoutRequest.refreshToken());
        refreshTokenService.delete(refreshToken);
    }
}
