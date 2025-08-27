package com.echobeat.music.dto.response;

import com.echobeat.music.entity.Artist;
import com.echobeat.music.enums.ArtistType;
import com.echobeat.music.enums.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistSummaryResponseDto {

    private Long id;
    private String name;
    private String displayName;
    private String profileImageUrl;
    private Country country;
    private ArtistType artistType;
    private String agency;
    private Boolean isActive;

    public static ArtistSummaryResponseDto from(Artist artist) {
        return ArtistSummaryResponseDto.builder()
            .id(artist.getId())
            .name(artist.getName())
            .displayName(artist.getDisplayName())
            .profileImageUrl(artist.getProfileImageUrl())
            .country(artist.getCountry())
            .artistType(artist.getArtistType())
            .agency(artist.getAgency())
            .isActive(artist.getIsActive())
            .build();
    }
}