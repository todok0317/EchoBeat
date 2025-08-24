package com.echobeat.music.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

// Artist.java - 아티스트 정보
@Entity
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;               // 기본 이름 (영문)

    @Column(name = "korean_name")
    private String koreanName;         // 한글명

    @Column(name = "japanese_name")
    private String japaneseName;       // 일본어명

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country country;           // KR, JP

    @Column(name = "debut_date")
    private LocalDate debutDate;

    @Column(name = "agency")
    private String agency;             // 소속사

    @Enumerated(EnumType.STRING)
    @Column(name = "artist_type")
    private ArtistType artistType;     // SOLO, GROUP, BAND

    @Column(name = "member_count")
    private Integer memberCount;       // 그룹 멤버 수

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
