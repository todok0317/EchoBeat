package com.echobeat.music.service;

import com.echobeat.music.dto.response.ChartEntryResponseDto;
import com.echobeat.music.dto.response.ChartRankingResponseDto;
import com.echobeat.music.dto.response.ChartResponseDto;
import com.echobeat.music.entity.Chart;
import com.echobeat.music.entity.ChartEntry;
import com.echobeat.music.repository.ChartEntryRepository;
import com.echobeat.music.repository.ChartRepository;
import com.echobeat.music.repository.TrackRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChartEntryService {

    private final ChartEntryRepository chartEntryRepository;
    private final ChartRepository chartRepository;
    private final TrackRepository trackRepository;

    // 특정 차트의 최신 순위 조회
    public ChartRankingResponseDto getLatestChartRanking(Long chartId, Integer topN) {
        Chart chart = chartRepository.findByIdOrElseThrow(chartId);

        List<ChartEntry> entries;
        if(topN != null && topN > 0) {
            entries = chartEntryRepository.findLatestTopN(chart, topN);
        } else {
            entries = chartEntryRepository.findLatestChartEntries(chart);
        }

        List<ChartEntryResponseDto> entryResponseDtos = entries.stream()
            .map(ChartEntryResponseDto::from)
            .collect(Collectors.toList());

        // 최신 차트 날짜 조회
        LocalDate latestDate = chartEntryRepository.findLatestChartDate(chart)
            .orElse(LocalDate.now());

        return ChartRankingResponseDto.builder()
            .chart(ChartResponseDto.from(chart))
            .entries(entryResponseDtos)
            .chartDate(latestDate)
            .totalEntries(entryResponseDtos.size())
            .build();

    }

    // 특정 날짜의 차트 순위 조회
    public ChartRankingResponseDto getChartRankingByDate(Long chartId, LocalDate chartDate) {
        Chart chart = chartRepository.findByIdOrElseThrow(chartId);
        List<ChartEntry> entries = chartEntryRepository.findByChartAndChartDateOrderByRankingAsc(
            chart, chartDate);

        List<ChartEntryResponseDto> entryDtos = entries.stream()
            .map(ChartEntryResponseDto::from)
            .collect(Collectors.toList());

        return ChartRankingResponseDto.builder()
            .chart(ChartResponseDto.from(chart))
            .entries(entryDtos)
            .chartDate(chartDate)
            .totalEntries(entryDtos.size())
            .build();
    }

    // 신규 진입곡들 조회
    public List<ChartEntryResponseDto> getNewEntries(Long chartId, LocalDate chartDate) {
        Chart chart = chartRepository.findByIdOrElseThrow(chartId);
        List<ChartEntry> entries = chartEntryRepository.findByChartAndChartDateAndIsNewEntryTrueOrderByRankingAsc(
            chart, chartDate);

        return entries.stream()
            .map(ChartEntryResponseDto::from)
            .collect(Collectors.toList());
    }

    // 상승곡들 조회
    public List<ChartEntryResponseDto> getRisingTracks(Long chartId, LocalDate chartDate) {
        Chart chart = chartRepository.findByIdOrElseThrow(chartId);
        List<ChartEntry> entries = chartEntryRepository.findRisingTracks(chart, chartDate);

        return entries.stream()
            .map(ChartEntryResponseDto::from)
            .collect(Collectors.toList());
    }

}
