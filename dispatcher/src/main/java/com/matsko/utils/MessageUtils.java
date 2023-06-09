package com.matsko.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Class that generates a response by creating a SendMessage object including ChatId and message text.
 */
@Component
public class MessageUtils {

    /**
     * Method that generates a response by creating a SendMessage object including ChatId and message text.
     *
     * @param text text outgoing message
     * @param update update in the chat
     * @return message
     */
    public SendMessage generateSendMessageWithText(Update update, String text) {
        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;
    }
}
