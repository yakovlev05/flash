package ru.yakovlev05.school.flash.mapper;

import org.springframework.stereotype.Component;
import ru.yakovlev05.school.flash.dto.auth.RegistrationRequest;
import ru.yakovlev05.school.flash.dto.user.UserResponse;
import ru.yakovlev05.school.flash.entity.User;

@Component
public class UserMapper {

    public UserResponse toDto(User user) {
        return new UserResponse(user.getUsername(), user.getEmail());
    }

    public User toEntity(RegistrationRequest dto){
        User user = new User();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        return user;
    }

}
