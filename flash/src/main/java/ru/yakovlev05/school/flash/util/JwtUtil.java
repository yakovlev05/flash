package ru.yakovlev05.school.flash.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.yakovlev05.school.flash.entity.User;
import ru.yakovlev05.school.flash.props.SecurityProps;
import ru.yakovlev05.school.flash.service.JwtAuthenticationService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final SecurityProps props;
    private final JwtAuthenticationService jwtAuthenticationService;

    private SecretKey getSignKey(String key) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    private String createToken(Map<String, Object> claims, String subject, SecretKey signKey, Long validityInSeconds) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validityInSeconds * 1000))
                .signWith(signKey)
                .compact();
    }

    public String generateAccessToken(User user) {
        return createToken(
                null,
                String.valueOf(user.getId()),
                getSignKey(props.getKeyForAccess()),
                props.getAccessTokenLifeTime()
        );
    }

    public String generateRefreshToken(User user) {
        return createToken(
                null,
                String.valueOf(user.getId()),
                getSignKey(props.getKeyForRefresh()),
                props.getRefreshTokenLifeTime()
        );
    }

    private boolean validateToken(String token, SecretKey verifyKey) {
        try {
            Jwts.parser()
                    .verifyWith(verifyKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, getSignKey(props.getKeyForAccess()));
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, getSignKey(props.getKeyForRefresh()));
    }

    private Claims getClaims(String token, SecretKey verifyKey) {
        return Jwts.parser()
                .verifyWith(verifyKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, getSignKey(props.getKeyForAccess()));
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, getSignKey(props.getKeyForRefresh()));
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getAccessClaims(accessToken);
        return jwtAuthenticationService.getJwtAuthentication(Long.parseLong(claims.getSubject()));
    }
}
