package com.project.packend.controller;

import com.project.packend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> request) {
        String to = request.get("to");
        String ccEmail = request.get("cc");
        String subject = request.get("subject");
        String content = request.get("content");

        // Serviceメール送信メソッド呼び出し
        emailService.sendSimpleEmail(to, ccEmail, subject, content);

        return ResponseEntity.ok("メールが成功に送信されました");
    }
}
