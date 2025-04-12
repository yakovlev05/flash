package ru.yakovlev05.school.flash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yakovlev05.school.flash.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void removeAllByUser_Id(Long userId);
}
