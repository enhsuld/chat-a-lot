package com.drestive.chatalot.service.common;

import com.drestive.chatalot.domain.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by mustafa on 11/05/2016.
 */
@Service
public class EmailService {

    public static final String REPLY_TO_ADDRESS = "dontreply@chatalot.com";
    public static final String FROM_ADDRESS = "registration@chatalot.com";

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(User user, String subject, String body) throws MessagingException {
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(user.getEmailAddress());
        helper.setReplyTo(REPLY_TO_ADDRESS);
        helper.setFrom(FROM_ADDRESS);
        helper.setSubject(subject);
        helper.setText(body);
        javaMailSender.send(mail);
    }
}
