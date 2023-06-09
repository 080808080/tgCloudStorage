package com.matsko.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Service for reading messages from the RabbitMQ broker.
 */
public interface ConsumerService {

    /**
     * Method for reading text messages from the RabbitMQ broker.
     *
     * @param update update in the chat.
     */
    void consumeTextMessageUpdates(Update update);

    /**
     * Method for reading document messages from the RabbitMQ broker.
     *
     * @param update update in the chat.
     */
    void consumeDocMessageUpdates(Update update);

    /**
     * Method for reading photo messages from the RabbitMQ broker.
     *
     * @param update update in the chat.
     */
    void consumePhotoMessageUpdates(Update update);
}