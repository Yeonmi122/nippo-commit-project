package com.project.packend.repository;

import com.project.packend.entity.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    // 作成日付の重複チェック
    boolean existsByReportDate(LocalDate reportDate);

    // 日報ステータス取得
    List<DailyReport> findByReportDateBetween(LocalDate start, LocalDate end);
}
