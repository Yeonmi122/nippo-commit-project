package com.project.packend.service;

import com.project.packend.dto.ReportResponse;
import com.project.packend.dto.StatusResponse;
import com.project.packend.entity.DailyReport;
import com.project.packend.entity.WeeklyReport;
import com.project.packend.repository.DailyReportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyReportService {

    private final DailyReportRepository dailyReportRepository;

    // 日報登録
    public DailyReport saveReport(DailyReport report) {
        if (dailyReportRepository.existsByReportDate(report.getReportDate())) {
            throw new IllegalArgumentException((report.getReportDate() + "日報は登録されています。"));
        }
        return dailyReportRepository.save(report);
    }

    // 日報情報取得
    public List<DailyReport> getAllReports() {
        return dailyReportRepository.findAll();
    }

    // 日報詳細取得
    public ReportResponse getReportById(Long id) {
        DailyReport entity = dailyReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + id + " に該当する日報が見つかりません"));
        return ReportResponse.from(entity);
    }

    // 日報ステータス取得
    public StatusResponse getStatus(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(startDate.lengthOfMonth());

        List<StatusResponse.StatusItem> items = dailyReportRepository.findByReportDateBetween(startDate, endDate)
                .stream()
                .map(entity -> new StatusResponse.StatusItem(
                        entity.getReportDate().toString(),
                        String.valueOf(entity.getId())
                ))
                .toList();

        return new StatusResponse(items);
    }
}
