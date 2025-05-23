package ru.yakovlev05.school.flash.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.yakovlev05.school.flash.service.NotificationSenderService;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationEmailSender implements NotificationSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${service.name}")
    private String nameService;


    @Override
    public void send(String text, String subject, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("%s <%s>".formatted(nameService, from));
        message.setSubject(subject);
        message.setText(text);
        message.setTo(email);

        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send email", e);
        }
    }
}
