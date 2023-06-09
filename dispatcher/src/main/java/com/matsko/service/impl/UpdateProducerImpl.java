package com.matsko.service.impl;

import com.matsko.service.UpdateProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Class that implements {@link UpdateProducer}.
 */
@Service
@Slf4j
public class UpdateProducerImpl implements UpdateProducer {

    /**
     * Field that accepts {@link RabbitTemplate}.
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructor.
     *
     * @param rabbitTemplate bean to send messages (producer).
     */
    public UpdateProducerImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}
