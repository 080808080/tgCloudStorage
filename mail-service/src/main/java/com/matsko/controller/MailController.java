package com.matsko.controller;

import com.matsko.dto.MailParams;
import com.matsko.service.MailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class-controller to process registration request from the user.
 */
@RestController
@RequestMapping("/mail")
public class MailController {
    private final MailSenderService mailSendService;

    /**
     * Constructor.
     *
     * @param mailSendService service for sending a response on registration request.
     */
    public MailController(MailSenderService mailSendService) {
        this.mailSendService = mailSendService;
    }

    /**
     * Method for receiving incoming post requests.
     *
     * @param mailParams establishes the correspondence of the user id and his mail.
     * @return response.
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams){
        mailSendService.send(mailParams);
        return ResponseEntity.ok().build();
    }
}
