package com.matsko.service;

import com.matsko.dto.MailParams;

/**
 * Service for sending a response on registration request.
 */
public interface MailSenderService {

    /**
     * Method that send message.
     *
     * @param mailParams establishes the correspondence of the user id and his mail.
     */
    void send(MailParams mailParams);
}
