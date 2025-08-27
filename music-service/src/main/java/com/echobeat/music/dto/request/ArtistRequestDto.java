package com.echobeat.music.dto.request;

import com.echobeat.music.enums.ArtistType;
import com.echobeat.music.enums.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistRequestDto {

    @NotBlank(message = "아티스트명은 필수입니다")
    private String name;

    private String koreanName;

    private String japaneseName;

    private String profileImageUrl;

    @NotNull(message = "국가는 필수입니다")
    private Country country;

    private LocalDate debutDate;

    private String agency;

    @NotNull(message = "아티스트 타입은 필수입니다")
    private ArtistType artistType;

    private Integer memberCount;

    private String description;
}