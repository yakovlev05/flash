package ru.yakovlev05.school.flash.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "security.jwt")
@Component
public class SecurityProps {
    private String keyForAccess;
    private String keyForRefresh;
    private Long accessTokenLifeTime;
    private Long refreshTokenLifeTime;
}
