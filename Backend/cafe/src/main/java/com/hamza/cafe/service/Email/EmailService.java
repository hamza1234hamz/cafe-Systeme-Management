package com.hamza.cafe.service.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;
    public void sendSimplMessage(String to, String Subject, String text,List<String> list){
        log.info("inside email service");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hamzaelgourchi98@gmail.com");
        message.setTo(to);
        message.setSubject(Subject);
        message.setText(text);
        if (list!=null && list.size() > 0){
            message.setCc(getCcArray(list));
        }
        log.info("sended message");
        javaMailSender.send(message);
    }
    private String[] getCcArray(List<String> ccList){
        String[] cc=new String[ccList.size()];
        for (int i=0;i<ccList.size();i++){
            cc[i]=ccList.get(i);
        }
        return cc;
    }
    public void forgotMail(String to,String subject,String password) throws MessagingException{
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setFrom("hamzaelgourchi98@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg,"text/html");
        javaMailSender.send(message);
    }

}
