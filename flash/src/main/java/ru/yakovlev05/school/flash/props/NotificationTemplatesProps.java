package ru.yakovlev05.school.flash.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "notification.templates")
public class NotificationTemplatesProps {
    private String newMessageText;
    private String newMessageTitle;
}
