package com.echobeat.music.controller;

import com.echobeat.common.dto.ApiResponse;
import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.service.CrawlingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Tag(name = "Crawling", description = "크롤링 관리 API (관리자용)")
@RestController
@RequestMapping("/admin/crawling")
@RequiredArgsConstructor
public class CrawlingController {

    private final CrawlingService crawlingService;

    @Operation(summary = "전체 차트 크롤링", description = "모든 활성화된 차트를 수동으로 크롤링합니다.")
    @PostMapping("/all")
    public ResponseEntity<ApiResponse<Void>> crawlAllCharts() {
        try {
            crawlingService.crawlAllCharts();
            return ResponseEntity.ok(ApiResponse.success("전체 차트 크롤링이 시작되었습니다."));
        } catch (Exception e) {
            log.error("전체 크롤링 실패", e);
            return ResponseEntity.ok(ApiResponse.error("크롤링 실행 중 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "소스별 크롤링", description = "특정 소스의 차트들을 크롤링합니다.")
    @PostMapping("/source/{source}")
    public ResponseEntity<ApiResponse<Void>> crawlBySource(
        @Parameter(description = "차트 소스") @PathVariable ChartSource source) {

        try {
            crawlingService.crawlBySource(source);
            return ResponseEntity.ok(ApiResponse.success(source + " 차트 크롤링이 시작되었습니다."));
        } catch (Exception e) {
            log.error("{} 크롤링 실패", source, e);
            return ResponseEntity.ok(ApiResponse.error("크롤링 실행 중 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "크롤러 상태 확인", description = "모든 크롤러의 사용 가능 상태를 확인합니다.")
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<ChartSource, Boolean>>> getCrawlerStatus() {
        try {
            Map<ChartSource, Boolean> status = crawlingService.getCrawlerStatus();
            return ResponseEntity.ok(ApiResponse.success("크롤러 상태 조회 완료", status));
        } catch (Exception e) {
            log.error("크롤러 상태 조회 실패", e);
            return ResponseEntity.ok(ApiResponse.error("상태 조회 중 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "멜론 차트 크롤링", description = "멜론 실시간 차트를 수동 크롤링합니다.")
    @PostMapping("/melon")
    public ResponseEntity<ApiResponse<Void>> crawlMelon() {
        try {
            crawlingService.crawlBySource(ChartSource.MELON);
            return ResponseEntity.ok(ApiResponse.success("멜론 차트 크롤링이 시작되었습니다."));
        } catch (Exception e) {
            log.error("멜론 크롤링 실패", e);
            return ResponseEntity.ok(ApiResponse.error("멜론 크롤링 중 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "Apple Music 차트 크롤링", description = "Apple Music 차트를 수동 크롤링합니다.")
    @PostMapping("/apple-music")
    public ResponseEntity<ApiResponse<Void>> crawlAppleMusic() {
        try {
            crawlingService.crawlBySource(ChartSource.APPLE_MUSIC);
            return ResponseEntity.ok(ApiResponse.success("Apple Music 차트 크롤링이 시작되었습니다."));
        } catch (Exception e) {
            log.error("Apple Music 크롤링 실패", e);
            return ResponseEntity.ok(ApiResponse.error("Apple Music 크롤링 중 오류가 발생했습니다."));
        }
    }
}