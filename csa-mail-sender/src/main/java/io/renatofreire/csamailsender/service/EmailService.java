package io.renatofreire.csamailsender.service;

import io.renatofreire.csamailsender.dto.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;


    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(final Mail mail) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariables(mail.getProperties());
            String htmlContent = templateEngine.process(mail.getTemplateName(), context);

            helper.setTo(mail.getMailTo());
            helper.setFrom(mail.getMailFrom());
            helper.setSubject(mail.getSubject());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("failed to send email", e);
        }
    }

}
