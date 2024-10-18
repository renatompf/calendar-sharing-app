package io.renatofreire.csamailsender.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renatofreire.csamailsender.configuration.RabbitConfiguration;
import io.renatofreire.csamailsender.dto.Mail;
import io.renatofreire.csamailsender.dto.RegisteredUserDto;
import io.renatofreire.csamailsender.dto.ShareCalendarMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class MailSenderService {

    private static final Logger logger = LoggerFactory.getLogger(MailSenderService.class);

    private final EmailService emailService;

    public MailSenderService(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitConfiguration.REGISTERED_QUEUE)
    public void registeredUser(Message message) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        RegisteredUserDto registeredUserDto = objectMapper.readValue(message.getBody(), RegisteredUserDto.class);
        logger.info("Registered user: {}", registeredUserDto);

        Mail mail = new Mail.MailBuilder()
                .mailFrom("no-reply@csa.io")
                .mailTo(registeredUserDto.email())
                .subject("Welcome to Calendar Sharing App")
                .templateName("emailValidation.html")
                .property("name", registeredUserDto.name())
                .property("link", "http://localhost:8080/auth/verify-email?token=" + registeredUserDto.validationToken())
                .build();

        emailService.sendEmail(mail);
    }

    @RabbitListener(queues = RabbitConfiguration.CALENDAR_SHARING_QUEUE)
    public void calendarSharing(Message message) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ShareCalendarMessageDto shareCalendarMessage = objectMapper.readValue(message.getBody(), ShareCalendarMessageDto.class);
        logger.info("Registered user: {}", shareCalendarMessage);

        switch (shareCalendarMessage.type()){
            case CREATION -> sendSharingCreationEmail(shareCalendarMessage);
            case DELETION -> sendSharingCancellationEmail(shareCalendarMessage);
        }

    }

    private void sendSharingCancellationEmail(ShareCalendarMessageDto shareCalendarMessage) {
        Mail mail = new Mail.MailBuilder()
                .mailFrom("no-reply@csa.io")
                .mailTo(shareCalendarMessage.sharedWithEmail())
                .subject("A Calendar Was Stopped Shared With You")
                .templateName("calendarNoMoreShared.html")
                .property("name", shareCalendarMessage.sharedWithName())
                .property("calendarName", shareCalendarMessage.calendarName())
                .build();

        emailService.sendEmail(mail);
    }

    private void sendSharingCreationEmail(ShareCalendarMessageDto shareCalendarMessage) {
        Mail mail = new Mail.MailBuilder()
                .mailFrom("no-reply@csa.io")
                .mailTo(shareCalendarMessage.sharedWithEmail())
                .subject("A Calendar Was Shared With You")
                .templateName("calendarShared.html")
                .property("name", shareCalendarMessage.sharedWithName())
                .property("calendarName", shareCalendarMessage.calendarName())
                .property("link", "http://localhost:8080/calendar-sharing/token-validation?token=" + shareCalendarMessage.token())
                .build();

        emailService.sendEmail(mail);
    }

}
