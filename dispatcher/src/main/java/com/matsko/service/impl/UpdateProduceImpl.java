package com.matsko.service.impl;

import com.matsko.service.UpdateProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
public class UpdateProduceImpl implements UpdateProducer {
    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
    }
}
