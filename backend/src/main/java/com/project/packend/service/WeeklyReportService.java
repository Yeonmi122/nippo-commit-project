package com.project.packend.service;

import com.project.packend.dto.ReportResponse;
import com.project.packend.dto.StatusResponse;
import com.project.packend.entity.WeeklyReport;
import com.project.packend.repository.WeeklyReportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;

    // 週報情報登録
    public WeeklyReport saveReport(WeeklyReport report) {
        if(weeklyReportRepository.existsByReportDate(report.getReportDate())) {
            throw new IllegalArgumentException((report.getReportDate() + "週報は登録されています。"));
        }
        return weeklyReportRepository.save(report);
    }

    // 週報情報取得
    public List<WeeklyReport> getAllReports() {
        return weeklyReportRepository.findAll();
    }

    // 週報詳細取得
    public ReportResponse getReportById(Long id) {
        WeeklyReport entity = weeklyReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + id + " に該当する週報が見つかりません"));
        return ReportResponse.from(entity);
    }

    // 週報ステータス取得
    public StatusResponse getStatus(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(startDate.lengthOfMonth());

        List<StatusResponse.StatusItem> items = weeklyReportRepository.findByReportDateBetween(startDate, endDate)
                .stream()
                .map(entity -> new StatusResponse.StatusItem(
                        entity.getReportDate().toString(),
                        String.valueOf(entity.getId())
                ))
                .toList();

        return new StatusResponse(items);
    }
}
