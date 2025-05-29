package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.entity.JwtAuthentication;
import ru.yakovlev05.school.flash.entity.User;
import ru.yakovlev05.school.flash.repository.UserRepository;
import ru.yakovlev05.school.flash.service.JwtAuthenticationService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {

    private final UserRepository userRepository;

    @Override
    public JwtAuthentication getJwtAuthentication(Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setUserId(userId);
        if (user != null) {
            jwtAuthentication.setAuthorities(List.of(new SimpleGrantedAuthority(user.getRole().toString())));
            jwtAuthentication.setAuthenticated(true);
        }

        return jwtAuthentication;
    }
}
