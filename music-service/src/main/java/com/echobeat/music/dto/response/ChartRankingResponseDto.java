package com.echobeat.music.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChartRankingResponseDto {
    private ChartResponseDto chart;
    private List<ChartEntryResponseDto> entries;
    private LocalDate chartDate;
    private Integer totalEntries;
}
