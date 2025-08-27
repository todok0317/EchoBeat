package com.echobeat.music.service;

import com.echobeat.music.dto.response.ChartEntryResponseDto;
import com.echobeat.music.dto.response.ChartRankingResponseDto;
import com.echobeat.music.dto.response.ChartResponseDto;
import com.echobeat.music.entity.Chart;
import com.echobeat.music.entity.ChartEntry;
import com.echobeat.music.entity.Track;
import com.echobeat.music.repository.ChartEntryRepository;
import com.echobeat.music.repository.ChartRepository;
import com.echobeat.music.repository.TrackRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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

    /**
     * 차트 엔트리 생성 (크롤링용)
     */
    public ChartEntry createChartEntry(Chart chart, Track track, Integer ranking,
                                     LocalDate chartDate, Long playCount) {
        
        // 중복 체크
        if (chartEntryRepository.existsByChartAndTrackAndChartDate(chart, track, chartDate)) {
            log.warn("이미 존재하는 차트 엔트리: chart={}, track={}, date={}", 
                chart.getName(), track.getTitle(), chartDate);
            return null;
        }
        
        // 이전 순위 조회 (어제 데이터가 있다면)
        LocalDate previousDate = chartDate.minusDays(1);
        List<ChartEntry> previousEntries = chartEntryRepository.findByChartAndChartDateOrderByRankingAsc(
            chart, previousDate);
        
        Integer previousRanking = previousEntries.stream()
            .filter(entry -> entry.getTrack().getId().equals(track.getId()))
            .map(ChartEntry::getRanking)
            .findFirst()
            .orElse(null);
        
        // 신규 진입 여부 결정
        boolean isNewEntry = previousRanking == null;
        
        ChartEntry chartEntry = ChartEntry.builder()
            .chart(chart)
            .track(track)
            .ranking(ranking)
            .previousRanking(previousRanking)
            .chartDate(chartDate)
            .playCount(playCount)
            .isNewEntry(isNewEntry)
            .build();
        
        ChartEntry saved = chartEntryRepository.save(chartEntry);
        log.info("새 차트 엔트리 생성: {} - {} ({}위)", chart.getName(), track.getTitle(), ranking);
        
        return saved;
    }

}
