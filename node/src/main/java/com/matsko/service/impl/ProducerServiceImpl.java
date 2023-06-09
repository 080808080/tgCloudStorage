package com.matsko.service.impl;

import com.matsko.service.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.matsko.model.RabbitQueue.ANSWER_MESSAGE;

/**
 * Class that implements {@link ProducerService}.
 */
@Service
public class ProducerServiceImpl implements ProducerService {

    /**
     * Field that accepts {@link RabbitTemplate}.
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructor.
     *
     * @param rabbitTemplate bean to send messages (producer).
     */
    public ProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void producerAnswer(SendMessage sendMessage) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
    }
}