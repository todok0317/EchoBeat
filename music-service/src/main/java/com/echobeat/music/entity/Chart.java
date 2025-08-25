package com.echobeat.music.entity;

import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.enums.Country;
import com.echobeat.music.enums.Genre;
import com.echobeat.music.enums.UpdateCycle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

// Chart.java - 차트 정보
@Getter
@Entity
@Table(name = "charts")
public class Chart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;               // "Apple Music J-Pop Top 100"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChartSource source;        // APPLE_MUSIC, MELON, BILLBOARD_JAPAN, ORICON

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country country;           // KR, JP

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;               // KPOP, JPOP

    @Enumerated(EnumType.STRING)
    @Column(name = "update_cycle")
    private UpdateCycle updateCycle;   // REAL_TIME, HOURLY, DAILY, WEEKLY

    @Column(name = "max_entries")
    private Integer maxEntries;        // 최대 순위 (100위까지 등)

    @Column(name = "is_active")
    private Boolean isActive = true;   // 활성화 여부

    @Column(name = "crawl_url", length = 500)
    private String crawlUrl;           // 크롤링 URL

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
