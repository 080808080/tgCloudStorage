package com.matsko.service;

import com.matsko.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
