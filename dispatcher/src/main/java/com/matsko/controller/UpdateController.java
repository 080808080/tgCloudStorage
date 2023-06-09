package com.matsko.controller;

import com.matsko.service.UpdateProducer;
import com.matsko.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.matsko.model.RabbitQueue.*;

/**
 * Class is a controller that distributes incoming messages from the user.
 */
@Component
@Slf4j
public class UpdateController {

    /**
     * Field that accepts {@link TelegramBot}.
     */
    private TelegramBot telegramBot;

    /**
     * Field that accepts {@link MessageUtils}.
     */
    private final MessageUtils messageUtils;

    /**
     * Field that accepts {@link UpdateProducer}.
     */
    private final UpdateProducer updateProducer;

    /**
     * Constructor.
     *
     * @param messageUtils class that generates a response by creating
     *                     a SendMessage object including ChatId and message text.
     * @param updateProducer service that sends updates to RabbitMQ.
     */
    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    /**
     * Method that connects UpdateController to the TelegramBot class.
     *
     * @param telegramBot class responsible for interacting with the telegram server.
     */
    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Method for primary validation of incoming data.
     *
     * @param update update in the chat.
     */
    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            distributeMessagesByType(update);
        } else {
            log.error("Unsupported message type is received: " + update);
        }
    }

    /**
     * Method that distributes messages according to the type of incoming data.
     *
     * @param update update in the chat.
     */
    private void distributeMessagesByType(Update update) {
        var message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    /**
     * Method that generates a response when a message of an unsupported type is received.
     *
     * @param update update in the chat.
     */
    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Неподдерживаемый тип сообщения!");
        setView(sendMessage);
    }

    /**
     * Method that returns an intermediate response about the received message.
     *
     * @param update update in the chat.
     */
    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Файл получен! Обрабатывается...");
        setView(sendMessage);
    }

    /**
     * Method for sending a reply to the Telegram.
     *
     * @param sendMessage reply message.
     */
    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    /**
     * Method that passes incoming photos to the correct queue in the RabbitMQ message broker.
     *
     * @param update update in the chat.
     */
    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    /**
     * Method that passes incoming documents to the correct queue in the RabbitMQ message broker.
     *
     * @param update update in the chat.
     */
    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    /**
     * Method that passes incoming text messages to the correct queue in the RabbitMQ message broker.
     *
     * @param update update in the chat.
     */
    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }
}
