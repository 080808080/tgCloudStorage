package com.matsko.service;

import com.matsko.entity.AppDocument;
import com.matsko.entity.AppPhoto;
import com.matsko.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
    String generationLink(Long docId, LinkType linkType);
}
