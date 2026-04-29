package com.project.packend.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.project.packend.entity.DailyReport;
import com.project.packend.entity.WeeklyReport;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonPropertyOrder({"id", "reportDate", "title", "content", "myAddress", "submitAddress", "createdAt"})
public class ReportResponse {
    private Long id;
    private LocalDate reportDate;
    private String title;
    private String content;
    private String myAddress;
    private String submitAddress;
    private LocalDateTime createAt;

    public static ReportResponse from(WeeklyReport entity) {
        return ReportResponse.builder()
                .id(entity.getId())
                .reportDate(entity.getReportDate())
                .title(entity.getSubject())
                .content(entity.getContent())
                .myAddress(entity.getUserId())
                .submitAddress(entity.getSubmitEmail())
                .createAt(entity.getCreatedAt())
                .build();
    }

    public static ReportResponse from(DailyReport entity) {
        return ReportResponse.builder()
                .id(entity.getId())
                .reportDate(entity.getReportDate())
                .title(entity.getSubject())
                .content(entity.getContent())
                .myAddress(entity.getUserId())
                .submitAddress(entity.getSubmitEmail())
                .createAt(entity.getCreatedAt())
                .build();
    }
}
