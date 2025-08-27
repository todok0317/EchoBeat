package com.echobeat.music.config;

import com.echobeat.music.entity.Chart;
import com.echobeat.music.enums.*;
import com.echobeat.music.repository.ChartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ChartRepository chartRepository;

    @Override
    public void run(ApplicationArguments args) {
        initializeCharts();
    }

    private void initializeCharts() {
        // 멜론 실시간 차트
        createChartIfNotExists(
            "멜론 실시간 TOP100",
            ChartSource.MELON,
            Country.KR,
            Genre.KPOP,
            UpdateCycle.REAL_TIME,
            100,
            "https://www.melon.com/chart/index.htm"
        );

        // Apple Music K-Pop 차트
        createChartIfNotExists(
            "Apple Music K-Pop Top 100",
            ChartSource.APPLE_MUSIC,
            Country.KR,
            Genre.KPOP,
            UpdateCycle.DAILY,
            100,
            null // API 사용
        );

        // Apple Music J-Pop 차트
        createChartIfNotExists(
            "Apple Music J-Pop Top 100",
            ChartSource.APPLE_MUSIC,
            Country.JP,
            Genre.JPOP,
            UpdateCycle.DAILY,
            100,
            null // API 사용
        );

        // Billboard Japan Hot 100
        createChartIfNotExists(
            "Billboard Japan Hot 100",
            ChartSource.BILLBOARD_JAPAN,
            Country.JP,
            Genre.JPOP,
            UpdateCycle.WEEKLY,
            100,
            "https://www.billboard-japan.com/charts/detail?a=hot100"
        );

        // Oricon 주간 차트
        createChartIfNotExists(
            "Oricon 주간 싱글 차트",
            ChartSource.ORICON,
            Country.JP,
            Genre.JPOP,
            UpdateCycle.WEEKLY,
            50,
            "https://www.oricon.co.jp/rank/js/w/"
        );

        log.info("차트 초기화 완료");
    }

    private void createChartIfNotExists(String name, ChartSource source, Country country,
        Genre genre, UpdateCycle updateCycle, Integer maxEntries,
        String crawlUrl) {

        if (chartRepository.findByName(name).isEmpty()) {
            Chart chart = Chart.builder()
                .name(name)
                .source(source)
                .country(country)
                .genre(genre)
                .updateCycle(updateCycle)
                .maxEntries(maxEntries)
                .isActive(true)
                .crawlUrl(crawlUrl)
                .build();

            chartRepository.save(chart);
            log.info("차트 생성: {}", name);
        }
    }
}
