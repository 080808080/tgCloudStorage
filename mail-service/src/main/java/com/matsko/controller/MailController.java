package com.matsko.controller;

import com.matsko.dto.MailParams;
import com.matsko.service.MailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {
    private final MailSenderService mailSendService;

    public MailController(MailSenderService mailSendService) {
        this.mailSendService = mailSendService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams){
        mailSendService.send(mailParams);
        return ResponseEntity.ok().build();
    }
}
