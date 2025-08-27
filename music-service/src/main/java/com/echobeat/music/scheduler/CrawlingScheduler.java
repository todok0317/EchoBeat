package com.echobeat.music.scheduler;

import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingScheduler {

    private final CrawlingService crawlingService;

    /**
     * 멜론 실시간 차트 - 5분마다
     */
    @Scheduled(fixedRate = 300000) // 5분
    public void crawlMelonRealtime() {
        log.info("멜론 실시간 차트 스케줄 크롤링 시작");
        try {
            crawlingService.crawlBySource(ChartSource.MELON);
        } catch (Exception e) {
            log.error("멜론 스케줄 크롤링 실패", e);
        }
    }

    /**
     * Apple Music 차트 - 1시간마다
     */
    @Scheduled(fixedRate = 3600000) // 1시간
    public void crawlAppleMusic() {
        log.info("Apple Music 차트 스케줄 크롤링 시작");
        try {
            crawlingService.crawlBySource(ChartSource.APPLE_MUSIC);
        } catch (Exception e) {
            log.error("Apple Music 스케줄 크롤링 실패", e);
        }
    }

    /**
     * Billboard Japan - 매일 오전 10시
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void crawlBillboardJapan() {
        log.info("Billboard Japan 차트 스케줄 크롤링 시작");
        try {
            crawlingService.crawlBySource(ChartSource.BILLBOARD_JAPAN);
        } catch (Exception e) {
            log.error("Billboard Japan 스케줄 크롤링 실패", e);
        }
    }

    /**
     * Oricon - 매주 화요일 오전 11시
     */
    @Scheduled(cron = "0 0 11 * * TUE")
    public void crawlOricon() {
        log.info("Oricon 주간 차트 스케줄 크롤링 시작");
        try {
            crawlingService.crawlBySource(ChartSource.ORICON);
        } catch (Exception e) {
            log.error("Oricon 스케줄 크롤링 실패", e);
        }
    }

    /**
     * 전체 차트 상태 점검 - 매일 자정
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void healthCheck() {
        log.info("크롤러 상태 점검 시작");
        try {
            var status = crawlingService.getCrawlerStatus();
            status.forEach((source, available) -> {
                log.info("크롤러 상태 - {}: {}", source, available ? "정상" : "사용불가");
            });
        } catch (Exception e) {
            log.error("크롤러 상태 점검 실패", e);
        }
    }
}