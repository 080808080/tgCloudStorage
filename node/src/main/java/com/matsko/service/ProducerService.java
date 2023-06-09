package com.matsko.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Service for sending responses from a node in the broker RabbitMQ.
 */
public interface ProducerService {


    /**
     * Method that sends responses from a node to the RabbitMQ broker.
     *
     * @param sendMessage text message.
     */
    void producerAnswer(SendMessage sendMessage);
}