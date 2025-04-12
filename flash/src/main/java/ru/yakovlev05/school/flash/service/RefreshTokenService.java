package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.entity.RefreshToken;
import ru.yakovlev05.school.flash.entity.User;

import java.util.Date;

public interface RefreshTokenService {
    void save(RefreshToken refreshTokenEntity);

    RefreshToken getByToken(String refreshToken);

    void delete(RefreshToken refreshToken);

    void save(String refreshToken, User user, Date expiredAt);
}
