package ru.yakovlev05.school.flash.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import ru.yakovlev05.school.flash.dto.UpdateUserRequest;
import ru.yakovlev05.school.flash.dto.UserResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @SecurityRequirement(name = "JWT")
    @GetMapping("/me")
    public UserResponse getMyInfo(@CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication) {
        return userService.getMyInfo(jwtAuthentication);
    }

    @SecurityRequirement(name = "JWT")
    @PutMapping("/me")
    public void updateMyInfo(@CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
                             @RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateMyInfo(jwtAuthentication, updateUserRequest);
    }

    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/me")
    public void deleteMyUser(@CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication){
        userService.deleteMyUser(jwtAuthentication);
    }
}
