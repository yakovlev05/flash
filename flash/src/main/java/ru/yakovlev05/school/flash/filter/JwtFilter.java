package ru.yakovlev05.school.flash.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.yakovlev05.school.flash.service.AuthService;
import ru.yakovlev05.school.flash.util.JwtUtil;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "access-token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = resolveToken(ACCESS_TOKEN_COOKIE_NAME, request);

        if (accessToken != null && jwtUtil.validateAccessToken(accessToken)) {

            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {

            String refreshToken = resolveToken(REFRESH_TOKEN_COOKIE_NAME, request);
            if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
                String newAccessToken = authService.refreshToken(refreshToken, response);

                Authentication authentication = jwtUtil.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(String cookieName, HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
