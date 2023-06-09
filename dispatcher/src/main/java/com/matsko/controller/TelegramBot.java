package com.matsko.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

/**
 * Class responsible for interacting with the telegram server.
 */
@Slf4j
@PropertySource("application.properties")
@Component
public class TelegramBot extends TelegramLongPollingBot {

    /**
     * Field that accepts the Telegram Bot username.
     */
    @Value("${bot.name}")
    private String botName;

    /**
     * Field that accepts the key to Telegram Bot.
     */
    @Value("${bot.token}")
    private String botToken;

    /**
     * Field that accepts {@link UpdateController}.
     */
    private final UpdateController updateController;

    /**
     * Method that connects TelegramBot to the UpdateController class.
     *
     * @param updateController controller that distributes incoming messages from the user.
     */
    public TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }

    /**
     * Method in which we pass a reference to the TelegramBot in the UpdateController.
     */
    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    /**
     * Class responsible for interacting with the telegram server.
     *
     * @return username of Telegram bot.
     */
    @Override
    public String getBotUsername() {
        return botName;
    }

    /**
     * Class responsible for interacting with the telegram server.
     *
     * @return API key of Telegram bot.
     */
    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * Method that processes the update in the chat.
     *
     * @param update update in the chat.
     */
    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    /**
     * Method sends a chat message to the user if it's not empty.
     *
     * @param message incoming message from the user.
     */
    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }
}
