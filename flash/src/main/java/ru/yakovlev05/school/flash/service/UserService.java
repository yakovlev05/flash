package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.entity.User;

public interface UserService {
    boolean existsByUsername(String username);

    void save(User user);

    User getByUsername(String username);
}
