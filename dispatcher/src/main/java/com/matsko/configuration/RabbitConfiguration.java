package com.matsko.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.matsko.model.RabbitQueue.*;

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

    /**
     * Method returns a pre-configured Bean that Spring places in its context.
     *
     * @return text message queue.
     */
    @Bean
    public Queue textMessageQueue() {
        return new Queue(TEXT_MESSAGE_UPDATE);
    }

    /**
     * Method returns a pre-configured Bean that Spring places in its context.
     *
     * @return document message queue.
     */
    @Bean
    public Queue docMessageQueue() {
        return new Queue(DOC_MESSAGE_UPDATE);
    }

    /**
     * Method returns a pre-configured Bean that Spring places in its context.
     *
     * @return photo message queue.
     */
    @Bean
    public Queue photoMessageQueue() {
        return new Queue(PHOTO_MESSAGE_UPDATE);
    }

    /**
     * Method returns a pre-configured Bean that Spring places in its context.
     *
     * @return answer message queue.
     */
    @Bean
    public Queue answerMessageQueue() {
        return new Queue(ANSWER_MESSAGE);
    }
}
