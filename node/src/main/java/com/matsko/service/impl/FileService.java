package com.matsko.service.impl;

import com.matsko.entity.AppDocument;
import com.matsko.entity.AppPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc (Message telegramMessage);
    AppPhoto processPhoto (Message telegramMessage);
}
