package com.project.packend.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "weekly_db")
public class WeeklyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 20, nullable = false)
    private String userId;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate; // DATE 타입 매핑

    @Column(length = 255, nullable = false)
    private String subject;

    @Column(name = "submit_email", length = 255, nullable = false)
    private String submitEmail;

    @Column(length = 255, nullable = false)
    private String cc;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public WeeklyReport(String userId, LocalDate reportDate, String subject,
                       String submitEmail, String cc, String content) {
        this.userId = userId;
        this.reportDate = reportDate;
        this.subject = subject;
        this.submitEmail = submitEmail;
        this.cc = cc;
        this.content = content;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
