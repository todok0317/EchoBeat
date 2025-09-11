package com.echobeat.music.entity;

import com.echobeat.music.enums.AlbumType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "albums")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "korean_title")
    private String koreanTitle;        // 한글 제목

    @Column(name = "japanese_title")
    private String japaneseTitle;      // 일본어 제목

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "album_type", nullable = false)
    private AlbumType albumType;       // SINGLE, EP, ALBUM

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;
    
    @Column(name = "total_tracks")
    private Integer totalTracks;       // 총 트랙 수
    
    @Column(name = "label")
    private String label;              // 발매사/레이블
    
    @Column(columnDefinition = "TEXT")
    private String description;        // 앨범 설명
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;   // 활성 상태

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Track> tracks = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 비즈니스 메서드
    public void updateAlbumInfo(String koreanTitle, String japaneseTitle, String coverImageUrl,
                               String label, String description) {
        this.koreanTitle = koreanTitle;
        this.japaneseTitle = japaneseTitle;
        this.coverImageUrl = coverImageUrl;
        this.label = label;
        this.description = description;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public String getDisplayTitle() {
        if (koreanTitle != null && !koreanTitle.trim().isEmpty()) {
            return koreanTitle;
        } else if (japaneseTitle != null && !japaneseTitle.trim().isEmpty()) {
            return japaneseTitle;
        }
        return title;
    }
    
    public void updateTotalTracks(Integer totalTracks) {
        this.totalTracks = totalTracks;
    }
    
    // setter 메서드들 (필요시 사용)
    public void setAlbumType(AlbumType albumType) {
        this.albumType = albumType;
    }
    
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
}
