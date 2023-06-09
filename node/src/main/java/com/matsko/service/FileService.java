package com.matsko.service;

import com.matsko.entity.AppDocument;
import com.matsko.entity.AppPhoto;
import com.matsko.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Service that receives the message and performs actions to download the file
 * and store it in the database.
 */
public interface FileService {

    /**
     * A method that makes an HTTP request to the Telegram server.
     * If the request is successful, the body is converted into an object containing JSON.
     * Then the path of the file is retrieved and a new request
     * to the Telegram server is made along the known path to download the file as an array of bytes.
     * Then an object is created, not linked to the database,
     * in which the received array of bytes is placed.
     * Then this object is stored in the database, and we get an object with a generated primary key.
     * From this object, the document is retrieved and stored in the database.
     *
     * @param telegramMessage incoming message.
     * @return data banked document.
     */
    AppDocument processDoc(Message telegramMessage);

    /**
     * A method that makes an HTTP request to the Telegram server.
     * If the request is successful, the body is converted into an object containing JSON.
     * Then the path of the file is retrieved and a new request
     * to the Telegram server is made along the known path to download the file as an array of bytes.
     * Then an object is created, not linked to the database,
     * in which the received array of bytes is placed.
     * Then this object is stored in the database, and we get an object with a generated primary key.
     * From this object, the photo is retrieved and stored in the database.
     *
     * @param telegramMessage incoming message.
     * @return data banked photo.
     */
    AppPhoto processPhoto(Message telegramMessage);

    /**
     * @param docId Telegram file ID.
     * @param linkType resource identifiers used in link generation.
     * @return link to download the file.
     */
    String generationLink(Long docId, LinkType linkType);
}
