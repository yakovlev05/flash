package ru.yakovlev05.school.flash.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.dto.user.UpdateUserRequest;
import ru.yakovlev05.school.flash.dto.user.UserResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.User;
import ru.yakovlev05.school.flash.exception.NotFoundException;
import ru.yakovlev05.school.flash.repository.UserRepository;
import ru.yakovlev05.school.flash.service.RefreshTokenService;
import ru.yakovlev05.school.flash.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь с логином '%s' не найден", username));
    }

    @Override
    public UserResponse getMyInfo(JwtAuthentication jwtAuthentication) {
        User user = getById(jwtAuthentication.getUserId());
        return new UserResponse(user.getUsername());
    }

    @Override
    public void updateMyInfo(JwtAuthentication jwtAuthentication, UpdateUserRequest updateUserRequest) {
        User user = getById(jwtAuthentication.getUserId());
        user.setPassword(passwordEncoder.encode(updateUserRequest.password()));
        user.setUsername(updateUserRequest.username());

        save(user);
    }

    @Transactional
    @Override
    public void deleteMyUser(JwtAuthentication jwtAuthentication) {
        User user = getById(jwtAuthentication.getUserId());
        user.setDeleted(true);
        refreshTokenService.revokeAllTokens(user.getId());
        save(user);
    }

    @Override
    public User getById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id '%d' не найден", id));
    }

    @Override
    public List<User> getListUsersByIds(List<Long> ids) {
        return userRepository.findByIdIn(ids);
    }

}
