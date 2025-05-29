package ru.yakovlev05.school.flash.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yakovlev05.school.flash.dto.auth.LoginRequest;
import ru.yakovlev05.school.flash.dto.auth.RegistrationRequest;
import ru.yakovlev05.school.flash.entity.RefreshToken;
import ru.yakovlev05.school.flash.entity.User;
import ru.yakovlev05.school.flash.exception.ConflictException;
import ru.yakovlev05.school.flash.exception.UnauthorizedException;
import ru.yakovlev05.school.flash.mapper.UserMapper;
import ru.yakovlev05.school.flash.props.SecurityProps;
import ru.yakovlev05.school.flash.service.AuthService;
import ru.yakovlev05.school.flash.service.RefreshTokenService;
import ru.yakovlev05.school.flash.service.UserService;
import ru.yakovlev05.school.flash.util.JwtUtil;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SecurityProps securityProps;

    @Transactional
    @Override
    public void registration(RegistrationRequest registrationRequest) {
        if (userService.existsByUsername(registrationRequest.username())) {
            throw new ConflictException("Имя пользователя '%s' занято", registrationRequest.username());
        }

        User user = userMapper.toEntity(registrationRequest);
        user.setPassword(passwordEncoder.encode(registrationRequest.password()));

        userService.save(user);
    }

    @Transactional
    @Override
    public void login(LoginRequest loginRequest, HttpServletResponse response) {
        User user = userService.getByUsername(loginRequest.username());
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UnauthorizedException("Неверный пароль");
        }

        String refreshToken = jwtUtil.generateRefreshToken(user);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUser(user);
        refreshTokenService.save(refreshTokenEntity);

        setCookie("refresh-token", refreshToken, securityProps.getRefreshTokenLifeTime(), response);
        setCookie("access-token", jwtUtil.generateAccessToken(user), securityProps.getAccessTokenLifeTime(), response);
    }

    @Transactional
    @Override
    public void logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;

        Cookie refreshCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refresh-token"))
                .findFirst()
                .orElse(null);
        if (refreshCookie == null) return;

        refreshTokenService.removeByToken(refreshCookie.getValue());
    }

    /**
     * Обновление токена
     *
     * @param validatedRefreshToken refresh токен, уже валидированный
     * @param response              http запрос
     * @return access token
     */
    @Transactional
    @Override
    public String refreshToken(String validatedRefreshToken, HttpServletResponse response) {
        refreshTokenService.removeByToken(validatedRefreshToken);

        Long userId = Long.parseLong(jwtUtil.getRefreshClaims(validatedRefreshToken).getSubject());
        User user = userService.getById(userId);

        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        String newAccessToken = jwtUtil.generateAccessToken(user);

        RefreshToken newRefreshTokenEntity = new RefreshToken();
        newRefreshTokenEntity.setToken(newRefreshToken);
        newRefreshTokenEntity.setUser(user);

        refreshTokenService.save(newRefreshTokenEntity);

        setCookie("refresh-token", newRefreshToken, securityProps.getRefreshTokenLifeTime(), response);
        setCookie("access-token", newAccessToken, securityProps.getAccessTokenLifeTime(), response);

        return newAccessToken;
    }

    private void setCookie(String cookieName, String token, Long maxAge, HttpServletResponse response) {
        // Подробнее о параметрах cookie https://developer.mozilla.org/ru/docs/Web/HTTP/Guides/Cookies
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge.intValue());
        cookie.setAttribute("SameSite", "Strict");
        cookie.setSecure(true);

        response.addCookie(cookie);
    }
}
