package com.matsko.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Service through which all incoming messages are processed.
 */
public interface MainService {

    /**
     * Method that receives an incoming text message.
     *
     * @param update update in the chat.
     */
    void processTextMessage(Update update);

    /**
     * Method that receives an incoming document message.
     *
     * @param update update in the chat.
     */
    void processDocMessage(Update update);

    /**
     * Method that receives an incoming photo message.
     *
     * @param update update in the chat.
     */
    void processPhotoMessage(Update update);
}