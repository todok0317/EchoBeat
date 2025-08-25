package com.echobeat.music.dto.response;

import com.echobeat.music.entity.Chart;
import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.enums.Country;
import com.echobeat.music.enums.Genre;
import com.echobeat.music.enums.UpdateCycle;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChartResponseDto {
    private Long id;
    private String name;
    private ChartSource source;
    private Country country;
    private Genre genre;
    private UpdateCycle updateCycle;
    private Integer maxEntries;
    private Boolean isActive;
    private LocalDateTime createdAt;

    public static ChartResponseDto from(Chart chart) {
        return ChartResponseDto.builder()
            .id(chart.getId())
            .name(chart.getName())
            .source(chart.getSource())
            .country(chart.getCountry())
            .genre(chart.getGenre())
            .updateCycle(chart.getUpdateCycle())
            .maxEntries(chart.getMaxEntries())
            .isActive(chart.getIsActive())
            .createdAt(chart.getCreatedAt())
            .build();
    }
}
