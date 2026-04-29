package com.project.packend.controller;

import com.project.packend.dto.ApiResponse;
import com.project.packend.dto.ReportResponse;
import com.project.packend.dto.StatusResponse;
import com.project.packend.entity.WeeklyReport;
import com.project.packend.service.WeeklyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class WeeklyReportController {

    private final WeeklyReportService weeklyReportService;

    // 週報情報登録
    @PostMapping("/api/weekly-reports")
    public ResponseEntity<ApiResponse> createReport(@RequestBody WeeklyReport report) {
        WeeklyReport saveReport = weeklyReportService.saveReport(report);

        ApiResponse response = new ApiResponse(saveReport.getId(), saveReport.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/shuho")
    public List<WeeklyReport> getAllReports() {
        return weeklyReportService.getAllReports();
    }

    // 週報詳細取得
    @GetMapping("/shuho/{report_id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable("report_id") Long reportId) {
        ReportResponse report = weeklyReportService.getReportById(reportId);
        return ResponseEntity.ok(report);
    }

    // 週報ステータス取得
    @GetMapping("/shuho/getShuhoStatus")
    public ResponseEntity<StatusResponse> getNippoStatus(
            @RequestParam("year") int year,
            @RequestParam("month") int month) {

        StatusResponse response = weeklyReportService.getStatus(year, month);
        return ResponseEntity.ok(response);
    }
}
