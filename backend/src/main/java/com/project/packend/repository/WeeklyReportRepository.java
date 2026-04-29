package com.project.packend.repository;

import com.project.packend.entity.DailyReport;
import com.project.packend.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {
    // 作成日付の重複チェック
    boolean existsByReportDate(LocalDate reportDate);

    // 週報ステータス取得
    List<WeeklyReport> findByReportDateBetween(LocalDate start, LocalDate end);
}
