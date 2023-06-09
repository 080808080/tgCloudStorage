package com.matsko.service;

import com.matsko.controller.UpdateController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Service that takes responses from RabbitMQ and passes to {@link UpdateController}.
 */
public interface AnswerConsumer {

    /**
     * Method that takes responses from RabbitMQ.
     *
     * @param sendMessage message at the message broker.
     */
    void consume(SendMessage sendMessage);
}