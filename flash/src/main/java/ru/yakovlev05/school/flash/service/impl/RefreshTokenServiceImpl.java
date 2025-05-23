package ru.yakovlev05.school.flash.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.entity.RefreshToken;
import ru.yakovlev05.school.flash.exception.NotFoundException;
import ru.yakovlev05.school.flash.repository.RefreshTokenRepository;
import ru.yakovlev05.school.flash.service.RefreshTokenService;
import ru.yakovlev05.school.flash.util.JwtUtil;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public void save(RefreshToken refreshTokenEntity) {
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    @Override
    public RefreshToken getByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("Refresh token '%s' не найден", refreshToken));
    }

    @Transactional
    @Override
    public void revokeAllTokens(Long userId) {
        refreshTokenRepository.removeAllByUser_Id(userId);
    }

    @Transactional
    @Override
    public void removeByToken(String refreshToken) {
        refreshTokenRepository.removeByToken(refreshToken);
    }
}
