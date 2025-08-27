package com.echobeat.music.service;

import com.echobeat.music.crawler.ChartCrawler;
import com.echobeat.music.dto.crawler.CrawledTrackDto;
import com.echobeat.music.entity.Chart;
import com.echobeat.music.entity.ChartEntry;
import com.echobeat.music.entity.Track;
import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.repository.ChartRepository;
import com.echobeat.music.service.ChartEntryService;
import com.echobeat.music.service.TrackService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final List<ChartCrawler> crawlers;
    private final ChartRepository chartRepository;
    private final TrackService trackService;
    private final ChartEntryService chartEntryService;

    private Map<ChartSource, ChartCrawler> crawlerMap;

    @PostConstruct
    private void initCrawlerMap() {
        crawlerMap = crawlers.stream()
            .collect(Collectors.toMap(ChartCrawler::getSource, crawler -> crawler));
    }


    // 모든 활성 차트 크롤링
    @Transactional
    public void crawlAllCharts() {
        log.info("전체 차트 크롤링 시작");

        List<Chart> activeCharts = chartRepository.findByIsActiveTrueOrderByNameAsc();

        for (Chart chart : activeCharts) {
            try {
                crawlSpecificChart(chart);
            } catch (Exception e) {
                log.error("차트 크롤링 실패: {}", chart.getName(), e);
            }
        }

        log.info("전체 차트 크롤링 완료");
    }

    // 특정 차트 크롤링
    @Transactional
    public void crawlSpecificChart(Chart chart) {
        ChartCrawler crawler = crawlerMap.get(chart.getSource());

        if (crawler == null) {
            log.warn("크롤러를 찾을 수 없음: {}", chart.getSource());
            return;
        }

        if (!crawler.isAvailable()) {
            log.warn("크롤러 사용 불가: {}", chart.getSource());
            return;
        }

        log.info("차트 크롤링 시작: {}", chart.getName());

        try {
            List<CrawledTrackDto> crawledTracks = crawler.crawlChart(chart);
            processCrawledTracks(chart, crawledTracks);

        } catch (Exception e) {
            log.error("차트 크롤링 중 오류: {}", chart.getName(), e);
        }
    }

    // 크롤링된 데이터 처리
    private void processCrawledTracks(Chart chart, List<CrawledTrackDto> crawledTracks) {
        LocalDate today = LocalDate.now();
        int processedCount = 0;

        for (CrawledTrackDto crawledTrack : crawledTracks) {
            try {
                // Track 생성 또는 조회
                Track track = trackService.findOrCreateTrack(
                    crawledTrack.getTitle(),
                    crawledTrack.getArtistName(),
                    crawledTrack.getAlbumName(),
                    crawledTrack.getThumbnailUrl(),
                    crawledTrack.getGenre()
                );

                // 외부 ID 업데이트
                if (crawledTrack.getAppleMusicId() != null) {
                    trackService.updateExternalIds(
                        track.getId(),
                        crawledTrack.getSpotifyId(),
                        crawledTrack.getAppleMusicId(),
                        crawledTrack.getYoutubeId()
                    );
                }

                // ChartEntry 생성
                ChartEntry chartEntry = chartEntryService.createChartEntry(
                    chart,
                    track,
                    crawledTrack.getRanking(),
                    today,
                    crawledTrack.getPlayCount()
                );

                if (chartEntry != null) {
                    processedCount++;
                }

            } catch (Exception e) {
                log.warn("트랙 처리 실패: {} - {}", crawledTrack.getTitle(), e.getMessage());
            }
        }

        log.info("차트 처리 완료: {} ({}/{})", chart.getName(), processedCount, crawledTracks.size());
    }

    // 소스별 크롤링
    public void crawlBySource(ChartSource source) {
        List<Chart> charts = chartRepository.findBySourceOrderByNameAsc(source);

        for (Chart chart : charts) {
            if (chart.getIsActive()) {
                crawlSpecificChart(chart);
            }
        }
    }

    //크롤러 상태 확인
    public Map<ChartSource, Boolean> getCrawlerStatus() {
        return crawlerMap.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().isAvailable()
            ));
    }
}