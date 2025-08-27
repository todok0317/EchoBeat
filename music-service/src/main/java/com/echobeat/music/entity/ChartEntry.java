package com.echobeat.music.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

// ChartEntry.java - 차트 엔트리 (순위 정보)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chart_entries",
    indexes = {
        @Index(name = "idx_chart_date_ranking", columnList = "chart_id, chart_date, ranking"),
        @Index(name = "idx_track_chart", columnList = "track_id, chart_id")
    }
)
public class ChartEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chart_id", nullable = false)
    private Chart chart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(nullable = false)
    private Integer ranking;           // 현재 순위

    @Column(name = "previous_ranking")
    private Integer previousRanking;   // 이전 순위

    @Column(name = "chart_date", nullable = false)
    private LocalDate chartDate;       // 차트 기준일

    @Column(name = "play_count")
    private Long playCount;            // 재생수 (있다면)

    @Column(name = "like_count")
    private Long likeCount;            // 좋아요수 (있다면)

    @CreationTimestamp
    @Column(name = "crawled_at")
    private LocalDateTime crawledAt;   // 크롤링한 시간

    @Column(name = "is_new_entry")
    private Boolean isNewEntry = false; // 신규 진입 여부
}
