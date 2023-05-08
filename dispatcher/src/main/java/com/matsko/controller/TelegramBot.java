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

@Slf4j
@PropertySource("application.properties")
@Component
public class TelegramBot extends TelegramLongPollingBot {

    static final String ERROR_TEXT = "Error occurred: ";

    private final UpdateController updateController;

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    public TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);

//        var incomingMessage = update.getMessage();
//        log.info(incomingMessage.getText());
//
//        var response = new SendMessage();
//        response.setChatId(incomingMessage.getChatId().toString());
//        response.setText("Hello");
//        answerMessage(response);
    }

    public void answerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(ERROR_TEXT + e.getMessage());
            }
        }
    }
}
