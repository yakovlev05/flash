package ru.yakovlev05.school.flash.service;

import ru.yakovlev05.school.flash.entity.JwtAuthentication;

public interface JwtAuthenticationService {
    JwtAuthentication getJwtAuthentication(Long userId);
}
