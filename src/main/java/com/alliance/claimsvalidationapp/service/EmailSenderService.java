package com.alliance.claimsvalidationapp.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender mailSender;

    public void sendEmail(String toEmail, String fromEmail, String subject, String body){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);

        mailSender.send(simpleMailMessage);

        System.out.print("Email Sent successfully");
    }
}
