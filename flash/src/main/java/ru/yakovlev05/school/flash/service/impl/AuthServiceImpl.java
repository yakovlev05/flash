package ru.yakovlev05.school.flash.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.dto.auth.LoginRequest;
import ru.yakovlev05.school.flash.dto.auth.LogoutRequest;
import ru.yakovlev05.school.flash.dto.auth.RegistrationRequest;
import ru.yakovlev05.school.flash.entity.RefreshToken;
import ru.yakovlev05.school.flash.entity.User;
import ru.yakovlev05.school.flash.exception.ConflictException;
import ru.yakovlev05.school.flash.exception.UnauthorizedException;
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
            throw new ConflictException("Имя пользователя '%s' занято", registrationRequest.username());
        }

        User user = new User();
        user.setUsername(registrationRequest.username());
        user.setPassword(passwordEncoder.encode(registrationRequest.password()));

        userService.save(user);
    }

    @Override
    public ResponseEntity<Void> login(LoginRequest loginRequest) {
        User user = userService.getByUsername(loginRequest.username());
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UnauthorizedException("Неверный пароль");
        }

        String refreshToken = jwtUtil.generateRefreshToken(user);
        Date refreshTokenExpiredAt = jwtUtil.getRefreshClaims(refreshToken).getExpiration();
        refreshTokenService.save(refreshToken, user, refreshTokenExpiredAt);

        // Подробнее о параметрах cookie https://developer.mozilla.org/ru/docs/Web/HTTP/Guides/Cookies
        ResponseCookie accessTokenCookie = ResponseCookie.from("access-token") // Сессионная
                .value(jwtUtil.generateAccessToken(user))
                .httpOnly(true)
                .sameSite("Strict")
                .secure(true)
                .path("/")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh-token")
                .value(jwtUtil.generateRefreshToken(user))
                .httpOnly(true)
                .maxAge(securityProps.getRefreshTokenLifeTime())
                .sameSite("Strict")
                .secure(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshCookie.toString())
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        RefreshToken refreshToken = refreshTokenService.getByToken(logoutRequest.refreshToken());
        refreshTokenService.delete(refreshToken);
    }

    /**
     * Обновление токена
     * @param validatedRefreshToken refresh токен, уже валидированный
     * @param response http запрос
     * @return access token
     */
    @Transactional
    @Override
    public String refreshToken(String validatedRefreshToken, HttpServletResponse response) {
        RefreshToken validatedRefreshTokenEntity = refreshTokenService.getByToken(validatedRefreshToken);
        refreshTokenService.delete(validatedRefreshTokenEntity);

        Long userId = Long.parseLong(jwtUtil.getRefreshClaims(validatedRefreshToken).getSubject());
        User user = userService.getById(userId);

        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        String newAccessToken = jwtUtil.generateAccessToken(user);

        RefreshToken newRefreshTokenEntity = new RefreshToken();
        newRefreshTokenEntity.setToken(newRefreshToken);
        newRefreshTokenEntity.setUser(user);

        refreshTokenService.save(newRefreshTokenEntity);

        // Отправка cookie
        Cookie refreshTokenCookie = new Cookie("refresh-token", newRefreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(securityProps.getRefreshTokenLifeTime().intValue());
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setSecure(true);

        Cookie accessTokenCookie = new Cookie("access-token", newAccessToken);
        refreshTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setAttribute("SameSite", "Strict");
        accessTokenCookie.setSecure(true);

        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);

        return newAccessToken;
    }
}
