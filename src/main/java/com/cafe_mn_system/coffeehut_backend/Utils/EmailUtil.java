package com.cafe_mn_system.coffeehut_backend.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;
    public void sendSimpleMessage(String to, String subject, String text, List<String> list){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        if (list != null && list.size() > 0){
            simpleMailMessage.setCc(getCcArray(list));
        }

        javaMailSender.send(simpleMailMessage);
    }

    private String[] getCcArray(List<String> list){
        String[] cc = new String[list.size()];

        for (int i = 0; i < list.size(); i++){
            cc[i] = list.get(i);
        }

        return cc;
    }


}
