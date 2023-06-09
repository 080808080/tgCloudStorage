package com.matsko.configuration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for integration with RabbitMQ.
 */
@Configuration
public class RabbitConfiguration {

    /**
     * Method that returns a converter that will convert updates to JSON and send them to RabbitMQ,
     * and receiving them back will convert them to Java objects.
     *
     * @return a converter that will convert updates to JSON and send them to RabbitMQ.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
