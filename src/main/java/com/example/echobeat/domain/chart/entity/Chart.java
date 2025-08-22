package com.example.echobeat.domain.chart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "charts")
public class Chart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;   // 곡명
    private String artist;  // 가수
    private int rank;       // 순위
    private String source;  // 출처 (예: Oricon, Spotify, Billboard)

    private LocalDateTime crawledAt; // 수집 시각
}

