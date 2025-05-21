package ru.yakovlev05.school.flash.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import ru.yakovlev05.school.flash.dto.user.UpdateUserRequest;
import ru.yakovlev05.school.flash.dto.user.UserResponse;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.service.UserService;

@Tag(name = "Пользователи", description = "API управления профилем")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить информацию об аутентифицированном пользователе")
    @SecurityRequirement(name = "JWT")
    @GetMapping("/me")
    public UserResponse getMyInfo(@CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication) {
        return userService.getMyInfo(jwtAuthentication);
    }

    @Operation(summary = "Обновить информацию")
    @SecurityRequirement(name = "JWT")
    @PutMapping("/me")
    public void updateMyInfo(@CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication,
                             @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateMyInfo(jwtAuthentication, updateUserRequest);
    }

    @Operation(summary = "Удалить свой аккаунт")
    @SecurityRequirement(name = "JWT")
    @DeleteMapping("/me")
    public void deleteMyUser(@CurrentSecurityContext(expression = "authentication") JwtAuthentication jwtAuthentication){
        userService.deleteMyUser(jwtAuthentication);
    }
}
