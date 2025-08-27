package com.echobeat.music.dto.request;

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
public class ArtistSearchRequestDto {

    private String keyword;
    private Country country;
    private ArtistType artistType;
    private String agency;
    private Boolean isActive;
    private String sortBy; // name, debutDate, popularity
    private String sortDirection; // asc, desc

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 20;
}
