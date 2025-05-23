package ru.yakovlev05.school.flash.eventlistener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.yakovlev05.school.flash.entity.User;
import ru.yakovlev05.school.flash.eventlistener.event.MessageCreatedEvent;
import ru.yakovlev05.school.flash.kafka.event.NotificationDirection;
import ru.yakovlev05.school.flash.kafka.event.NotificationEvent;
import ru.yakovlev05.school.flash.props.NotificationTemplatesProps;
import ru.yakovlev05.school.flash.service.NotificationService;
import ru.yakovlev05.school.flash.service.UserService;

@RequiredArgsConstructor
@Component
public class GlobalEventListener {

    private final NotificationService notificationService;
    private final NotificationTemplatesProps notificationTemplatesProps;

    private final UserService userService;

    @EventListener
    public void handleMessageCreatedEvent(MessageCreatedEvent event) {
        User sender = userService.getById(event.senderId());

        NotificationEvent notificationEvent = new NotificationEvent(
                notificationTemplatesProps.getNewMessageText().formatted(sender.getUsername(), event.text()),
                notificationTemplatesProps.getNewMessageTitle().formatted(sender.getUsername()),
                sender.getEmail(),
                NotificationDirection.EMAIL
        );

        notificationService.send(notificationEvent);
    }
}
