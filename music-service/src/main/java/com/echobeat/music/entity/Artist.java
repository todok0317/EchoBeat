package com.echobeat.music.entity;

import com.echobeat.music.enums.ArtistType;
import com.echobeat.music.enums.Country;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(columnDefinition = "TEXT")
    private String description;        // 아티스트 설명

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;   // 활동 중 여부

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TrackArtist> trackArtists = new ArrayList<>();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Album> albums = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 비즈니스 메서드
    public void updateProfile(String koreanName, String japaneseName, String profileImageUrl, 
                            String agency, String description) {
        this.koreanName = koreanName;
        this.japaneseName = japaneseName;
        this.profileImageUrl = profileImageUrl;
        this.agency = agency;
        this.description = description;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public String getDisplayName() {
        if (koreanName != null && !koreanName.trim().isEmpty()) {
            return koreanName;
        } else if (japaneseName != null && !japaneseName.trim().isEmpty()) {
            return japaneseName;
        }
        return name;
    }
}
