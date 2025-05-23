package ru.yakovlev05.school.flash.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "kafka.producer")
public class KafkaTopicProps {
    private String notification;
}
