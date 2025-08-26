package com.echobeat.music.service;

import com.echobeat.music.dto.response.ChartListResponseDto;
import com.echobeat.music.dto.response.ChartResponseDto;
import com.echobeat.music.entity.Chart;
import com.echobeat.music.enums.Genre;
import com.echobeat.music.repository.ChartRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChartService {

    private final ChartRepository chartRepository;

    // 모든 활성화 된 차트 조회
    public ChartListResponseDto getAllActiveCharts() {
        List<Chart> charts = chartRepository.findByIsActiveTrueOrderByNameAsc();
        List<ChartResponseDto> chartResponseDtos = charts.stream()
            .map(ChartResponseDto::from)
            .collect(Collectors.toList());

        return ChartListResponseDto.builder()
            .charts(chartResponseDtos)
            .totalCount(chartResponseDtos.size())
            .build();
    }

    // 장르별 차트 조회
    public ChartListResponseDto getChartsByGenre(Genre genre) {
        List<Chart> charts = chartRepository.findByGenreAndIsActiveTrueOrderByNameAsc(genre);
        List<ChartResponseDto> chartResponseDtos = charts.stream()
            .map(ChartResponseDto::from)
            .collect(Collectors.toList());

        return ChartListResponseDto.builder()
            .charts(chartResponseDtos)
            .totalCount(chartResponseDtos.size())
            .build();
    }

    // K-Pop 차트들 조회
    public ChartListResponseDto getKPopCharts() {
        return getChartsByGenre(Genre.KPOP);
    }

    // J-Pop 차트들 조회
    public ChartListResponseDto getJPopCharts() {
        return getChartsByGenre(Genre.JPOP);
    }

}
