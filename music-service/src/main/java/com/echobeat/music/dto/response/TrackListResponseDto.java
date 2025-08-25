package com.echobeat.music.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrackListResponseDto {
    private List<TrackResponseDto> tracks;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Boolean hasNext;
}
