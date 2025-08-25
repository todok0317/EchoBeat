package com.echobeat.music.dto.response;

import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.enums.Country;
import com.echobeat.music.enums.Genre;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChartListResponseDto {
    private List<ChartResponseDto> charts;
    private Integer totalCount;
    private Genre genre;      // 필터링 정보
    private Country country;  // 필터링 정보
    private ChartSource source; // 필터링 정보
}