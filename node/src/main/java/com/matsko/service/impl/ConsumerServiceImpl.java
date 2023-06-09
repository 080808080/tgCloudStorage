package com.matsko.service.impl;

import com.matsko.service.ConsumerService;
import com.matsko.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.matsko.model.RabbitQueue.*;

/**
 * Class that implements {@link ConsumerService}.
 */
@Service
@Slf4j
public class ConsumerServiceImpl implements ConsumerService {

    /**
     * Field that accepts {@link MainService}.
     */
    private final MainService mainService;

    /**
     * Constructor.
     *
     * @param mainService service through which all incoming messages are processed.
     */
    public ConsumerServiceImpl(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");
        mainService.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdates(Update update) {
        log.debug("NODE: Doc message is received");
        mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: Photo message is received");
        mainService.processPhotoMessage(update);
    }
}