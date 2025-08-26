package com.echobeat.music.repository;

import com.echobeat.music.entity.Chart;
import com.echobeat.music.entity.ChartEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartEntryRepository extends JpaRepository<ChartEntry, Long> {

    // 특정 차트의 Top N 조회
    @Query("SELECT ce FROM ChartEntry ce WHERE ce.chart = :chart " +
        "AND ce.chartDate = (SELECT MAX(ce2.chartDate) FROM ChartEntry ce2 WHERE ce2.chart = :chart) " +
        "AND ce.ranking <= :topN ORDER BY ce.ranking ASC")
    List<ChartEntry> findLatestTopN(@Param("chart") Chart chart, @Param("topN") int topN);

    // 특정 차트의 최신 순위 조회
    @Query("SELECT ce FROM ChartEntry ce WHERE ce.chart = :chart " +
        "AND ce.chartDate = (SELECT MAX(ce2.chartDate) FROM ChartEntry ce2 WHERE ce2.chart = :chart) " +
        "ORDER BY ce.ranking ASC")
    List<ChartEntry> findLatestChartEntries(@Param("chart") Chart chart);

    // 최신 차트 날짜 조회
    @Query("SELECT MAX(ce.chartDate) FROM ChartEntry ce WHERE ce.chart = :chart")
    Optional<LocalDate> findLatestChartDate(@Param("chart") Chart chart);

    // 특정 차트의 특정 날짜 순위 조회 (순위 오름차순)
    List<ChartEntry> findByChartAndChartDateOrderByRankingAsc(Chart chart, LocalDate chartDate);

    // 신규 진입곡들 조회
    List<ChartEntry> findByChartAndChartDateAndIsNewEntryTrueOrderByRankingAsc(
        Chart chart, LocalDate chartDate);

    // 상승곡 조회 (이전 순위보다 현재 순위가 높은 곡들)
    @Query("SELECT ce FROM ChartEntry ce WHERE ce.chart = :chart AND ce.chartDate = :chartDate " +
        "AND ce.previousRanking IS NOT NULL AND ce.ranking < ce.previousRanking " +
        "ORDER BY (ce.previousRanking - ce.ranking) DESC")
    List<ChartEntry> findRisingTracks(@Param("chart") Chart chart, @Param("chartDate") LocalDate chartDate);


}
