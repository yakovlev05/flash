package ru.yakovlev05.school.flash.mapper;

import org.springframework.stereotype.Component;
import ru.yakovlev05.school.flash.dto.user.UserResponse;
import ru.yakovlev05.school.flash.entity.User;

@Component
public class UserMapper {

    public UserResponse toDto(User user) {
        return new UserResponse(user.getUsername());
    }

}
