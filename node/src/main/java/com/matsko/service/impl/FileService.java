package com.matsko.service.impl;

import com.matsko.entity.AppDocument;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc (Message externalMessage);
}
