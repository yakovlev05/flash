package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.kafka.event.NotificationEvent;
import ru.yakovlev05.school.flash.props.KafkaTopicProps;
import ru.yakovlev05.school.flash.service.NotificationService;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicProps kafkaTopicProps;

    @Override
    public void send(NotificationEvent event) {
        kafkaTemplate.send(kafkaTopicProps.getNotification(), event)
                .exceptionally(ex -> {
                    log.error("Failed to send notification", ex);
                    return null;
                });
    }

}
