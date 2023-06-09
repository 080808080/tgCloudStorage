package com.matsko.service.impl;

import com.matsko.controller.UpdateController;
import com.matsko.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.matsko.model.RabbitQueue.ANSWER_MESSAGE;

/**
 * Class that implements {@link AnswerConsumer}.
 */
@Service
public class AnswerConsumerImpl implements AnswerConsumer {

    /**
     * Field that accepts {@link UpdateController}.
     */
    private final UpdateController updateController;

    /**
     * Constructor.
     *
     * @param updateController controller that distributes incoming messages from the user.
     */
    public AnswerConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);
    }
}
