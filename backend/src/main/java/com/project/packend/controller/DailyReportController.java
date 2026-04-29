package com.project.packend.controller;

import com.project.packend.dto.ApiResponse;
import com.project.packend.dto.ReportResponse;
import com.project.packend.dto.StatusResponse;
import com.project.packend.entity.DailyReport;
import com.project.packend.entity.WeeklyReport;
import com.project.packend.service.DailyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DailyReportController {

    private final DailyReportService dailyReportService;

    // 日報情報登録
    @PostMapping("/api/daily-reports")
    public ResponseEntity<ApiResponse> createReport(@RequestBody DailyReport report) {
        DailyReport saveReport = dailyReportService.saveReport(report);

        ApiResponse response = new ApiResponse(saveReport.getId(), saveReport.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/nippo")
    public List<DailyReport> getReports() {
        return dailyReportService.getAllReports();
    }

    // 日報詳細取得
    @GetMapping("/nippo/{report_id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable("report_id") Long reportId) {
        ReportResponse report = dailyReportService.getReportById(reportId);
        return ResponseEntity.ok(report);
    }

    // 日報ステータス取得
    @GetMapping("/nippo/getNippoStatus")
    public ResponseEntity<StatusResponse> getNippoStatus(
            @RequestParam("year") int year,
            @RequestParam("month") int month) {

        StatusResponse response = dailyReportService.getStatus(year, month);
        return ResponseEntity.ok(response);
    }
}
