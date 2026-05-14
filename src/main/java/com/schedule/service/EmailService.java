package com.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendShareNotification(String toEmail, String scheduleName, String fromName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[일정 공유] " + fromName + "님이 일정을 공유했습니다.");
        message.setText("'" + scheduleName + "' 일정이 공유되었습니다.\n일정 관리 서비스에 접속해서 확인해보세요!");
        mailSender.send(message);
    }
}