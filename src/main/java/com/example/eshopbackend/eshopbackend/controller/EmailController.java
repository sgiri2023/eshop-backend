package com.example.eshopbackend.eshopbackend.controller;

import com.example.eshopbackend.eshopbackend.datamodel.Email.EmailRequest;
import com.example.eshopbackend.eshopbackend.service.Email.EmailService;
import com.example.eshopbackend.eshopbackend.service.impl.Email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    EmailService emailService;

    // http://localhost:8090/api/email/sendMail
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailRequest details)
    {
        String status
                = emailService.sendSimpleMail(details);
        return status;
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailRequest details)
    {
        String status
                = emailService.sendMailWithAttachment(details);

        return status;
    }
}
