package com.project.packend.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlEmail(String fromEmail, String toEmail, String ccEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // 文字化け防止(UTF-8)およびふぃある添付などが可能なオブジェクト生成
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // メール情報セット
            helper.setTo(toEmail);

            if(ccEmail != null && !ccEmail.trim().isEmpty()) {
                helper.setCc(ccEmail);
            }

            helper.setSubject((subject));

            helper.setText(htmlContent, true);
            helper.setFrom(fromEmail);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("メール送信に失敗しました:" + e.getMessage());
        }
    }
}
