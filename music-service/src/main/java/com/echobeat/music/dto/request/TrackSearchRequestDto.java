package com.echobeat.music.dto.request;

import com.echobeat.music.enums.Genre;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackSearchRequestDto {
    private String keyword;
    private Genre genre;

    @Min(0)
    private int page = 0;

    @Min(1) @Max(100)
    private int size = 20;
}
