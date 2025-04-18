package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.dto.UpdateUserRequest;
import ru.yakovlev05.school.flash.dto.UserResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.User;

import java.util.List;

public interface UserService {
    boolean existsByUsername(String username);

    void save(User user);

    User getByUsername(String username);

    UserResponse getMyInfo(JwtAuthentication jwtAuthentication);

    void updateMyInfo(JwtAuthentication jwtAuthentication, UpdateUserRequest updateUserRequest);

    void deleteMyUser(JwtAuthentication jwtAuthentication);

    User getById(Long id);

    List<User> getListUsersByIds(List<Long> ids);
}
