package com.matsko.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Service that sends updates to RabbitMQ.
 */
public interface UpdateProducer {

    /**
     * Method that sends updates to RabbitMQ.
     *
     * @param rabbitQueue queue at the message broker RabbitMQ.
     * @param update update in the chat.
     */
    void produce(String rabbitQueue, Update update);
}
