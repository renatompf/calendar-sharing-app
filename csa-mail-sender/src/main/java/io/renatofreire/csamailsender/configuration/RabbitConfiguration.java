package io.renatofreire.csamailsender.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfiguration {

    public static final String REGISTERED_QUEUE = "registered_queue";

    public static final String CALENDAR_SHARING_QUEUE = "calendar_sharing_queue";

    public static final String EXCHANGE = "exchange";

    @Bean
    Queue messageQueue(){
        return new Queue(REGISTERED_QUEUE);
    }

    @Bean
    Queue replyQueue(){
        return new Queue(CALENDAR_SHARING_QUEUE);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Binding msgBinding() {
        return BindingBuilder.bind(messageQueue()).to(exchange()).with(REGISTERED_QUEUE);
    }

    @Bean
    Binding replyBinding() {
        return BindingBuilder.bind(replyQueue()).to(exchange()).with(CALENDAR_SHARING_QUEUE);
    }

}