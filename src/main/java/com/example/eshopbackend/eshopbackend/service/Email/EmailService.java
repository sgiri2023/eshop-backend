package com.example.eshopbackend.eshopbackend.service.Email;

import com.example.eshopbackend.eshopbackend.datamodel.Email.EmailRequest;

public interface EmailService {
    String sendSimpleMail(EmailRequest details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailRequest details);
}
