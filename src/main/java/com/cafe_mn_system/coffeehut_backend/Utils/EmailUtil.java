package com.cafe_mn_system.coffeehut_backend.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        if (list != null && list.size() > 0) {
            simpleMailMessage.setCc(getCcArray(list));
        }

        javaMailSender.send(simpleMailMessage);
    }

    private String[] getCcArray(List<String> list) {
        String[] cc = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            cc[i] = list.get(i);
        }

        return cc;
    }

    public void forgotMail(String to, String subject, String password) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for CoffeeHut Cafe Portal </b><br><br><b>" +
                "Email : </b> " + to + " <br><b>" +
                "Password : </b> " + password + "<br><br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        mimeMessage.setContent(htmlMsg,"text/html");
        javaMailSender.send(mimeMessage);

    }

}
