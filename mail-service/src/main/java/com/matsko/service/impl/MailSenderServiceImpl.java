package com.matsko.service.impl;

import com.matsko.dto.MailParams;
import com.matsko.service.MailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Class that implements {@link MailSenderService}.
 */
@Service
public class MailSenderServiceImpl implements MailSenderService {

    /**
     * Field that accepts {@link JavaMailSender}.
     */
    private final JavaMailSender javaMailSender;

    /**
     * Field that contained bots email.
     */
    @Value("${spring.mail.username}")
    private String emailFrom;

    /**
     * Field that contained activation link template.
     */
    @Value("${service.activation.uri}")
    private String activationServiceUri;

    /**
     * Constructor.
     *
     * @param javaMailSender extended {@link MailSender} interface for JavaMail,
     *                      supporting MIME messages both as direct arguments
     *                      and through preparation callbacks.
     */
    public MailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(MailParams mailParams) {
        var subject = "Активация учетной записи";
        var messageBody = getActivationMailBody(mailParams.getId());
        var emailTo = mailParams.getEmailTo();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        javaMailSender.send(mailMessage);
    }

    /**
     * Method that returns the text of the activation letter.
     *
     * @param id user id.
     * @return text of the activation letter.
     */
    private String getActivationMailBody(String id) {
        var msg = String.format("Для завершения регистрации перейдите по ссылке:\n%s",
                activationServiceUri);
        return msg.replace("{id}", id);
    }
}