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
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

// Track.java - 곡 정보
@Entity
@Getter
@Table(name = "tracks")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;              // 곡 제목

    @Column(name = "artist_name", nullable = false)
    private String artistName;         // 아티스트명 (정규화 전 임시)

    @Column(name = "album_name")
    private String albumName;          // 앨범명

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;       // 썸네일 이미지 URL

    @Column(name = "preview_url", length = 500)
    private String previewUrl;         // 미리듣기 URL

    // 외부 서비스 ID 매핑
    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "apple_music_id")
    private String appleMusicId;

    @Column(name = "youtube_id")
    private String youtubeId;

    @Column(name = "release_date")
    private LocalDate releaseDate;     // 발매일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;               // KPOP, JPOP

    @Column(name = "duration_ms")
    private Integer durationMs;        // 재생 시간 (밀리초)

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
