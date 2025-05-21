package ru.yakovlev05.school.flash.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ticket")
@Getter
@Setter
public class TicketProps {
    private Long timeToLiveInSeconds;
}
