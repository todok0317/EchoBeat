package com.echobeat.music.dto.response;

import com.echobeat.music.entity.Artist;
import com.echobeat.music.enums.ArtistType;
import com.echobeat.music.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponseDto {

    private Long id;
    private String name;
    private String koreanName;
    private String japaneseName;
    private String displayName;
    private String profileImageUrl;
    private Country country;
    private LocalDate debutDate;
    private String agency;
    private ArtistType artistType;
    private Integer memberCount;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ArtistResponseDto from(Artist artist) {
        return ArtistResponseDto.builder()
            .id(artist.getId())
            .name(artist.getName())
            .koreanName(artist.getKoreanName())
            .japaneseName(artist.getJapaneseName())
            .displayName(artist.getDisplayName())
            .profileImageUrl(artist.getProfileImageUrl())
            .country(artist.getCountry())
            .debutDate(artist.getDebutDate())
            .agency(artist.getAgency())
            .artistType(artist.getArtistType())
            .memberCount(artist.getMemberCount())
            .description(artist.getDescription())
            .isActive(artist.getIsActive())
            .createdAt(artist.getCreatedAt())
            .updatedAt(artist.getUpdatedAt())
            .build();
    }
}