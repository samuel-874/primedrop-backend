package com.jme.spatch.backend.config.mail;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static com.jme.spatch.backend.config.mail.EmailTemplate.getRegMail;


@Service
public class MailSenderService {

    private final JavaMailSender mailSender;

    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public   void sendMail(String email,String url)
            throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setSubject("verify email");
        helper.setTo(email);
        helper.setFrom( new InternetAddress("samuelab846@gmail.com","Spatch"));
        helper.setText(getRegMail(url),true);


                mailSender.send(message);
    }
}
