package com.echobeat.music.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistUpdateRequestDto {

    private String koreanName;

    private String japaneseName;

    private String profileImageUrl;

    private String agency;

    private String description;

}
