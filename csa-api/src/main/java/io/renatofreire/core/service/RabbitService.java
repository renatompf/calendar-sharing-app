package io.renatofreire.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renatofreire.core.configuration.RabbitConfiguration;
import io.renatofreire.core.dto.requests.RegisteredUserMessageDto;
import io.renatofreire.core.dto.requests.ShareCalendarMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class RabbitService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitService.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(ShareCalendarMessageDto operationMessage)  {
        try{
            Message messageToSend = MessageBuilder
                    .withBody(objectMapper.writeValueAsString(operationMessage).getBytes())
                    .setCorrelationId(UUID.randomUUID().toString())
                    .build();

            logger.info("sent message = {}", messageToSend);
            rabbitTemplate.send(RabbitConfiguration.EXCHANGE, RabbitConfiguration.CALENDAR_SHARING_QUEUE, messageToSend);

        }catch (IOException exception){
            logger.error("Something went wrong while communication with the broker or serializing the messages. Exception: " + exception.getMessage());
        }

    }

    public void sendMessage(RegisteredUserMessageDto operationMessage)  {
        try{
            Message messageToSend = MessageBuilder
                    .withBody(objectMapper.writeValueAsString(operationMessage).getBytes())
                    .setCorrelationId(UUID.randomUUID().toString())
                    .build();

            logger.info("sent message = {}", messageToSend);
            rabbitTemplate.send(RabbitConfiguration.EXCHANGE, RabbitConfiguration.REGISTERED_QUEUE, messageToSend);

        }catch (IOException exception){
            logger.error("Something went wrong while communication with the broker or serializing the messages. Exception: " + exception.getMessage());
        }

    }

}
