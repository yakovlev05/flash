package ru.yakovlev05.school.flash.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.yakovlev05.school.flash.event.NotificationEvent;
import ru.yakovlev05.school.flash.service.NotificationSenderService;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationListener {


    private final NotificationSenderService notificationEmailSender;

    @KafkaListener(topics = "${kafka.consumer.notification.topic}",
            groupId = "${kafka.consumer.notification.group-id}",
            containerFactory = "notificationListenerContainerFactory")
    public void onMessage(@Payload NotificationEvent notificationEvent) {
        log.info("Received notification: {}", notificationEvent);

        switch (notificationEvent.direction()) {
            case EMAIL -> notificationEmailSender.send(
                    notificationEvent.text(),
                    notificationEvent.title(),
                    notificationEvent.destination());
        }
    }
}
