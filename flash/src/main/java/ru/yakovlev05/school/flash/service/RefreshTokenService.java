package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.entity.RefreshToken;
import ru.yakovlev05.school.flash.entity.User;

import java.util.Date;

public interface RefreshTokenService {
    void save(RefreshToken refreshTokenEntity);

    RefreshToken getByToken(String refreshToken);

    void revokeAllTokens(Long userId);

    void removeByToken(String refreshToken);
}
