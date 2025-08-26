package com.echobeat.music.controller;

import com.echobeat.common.dto.ApiResponse;
import com.echobeat.music.dto.response.ChartListResponseDto;
import com.echobeat.music.dto.response.ChartRankingResponseDto;
import com.echobeat.music.enums.Genre;
import com.echobeat.music.service.ChartEntryService;
import com.echobeat.music.service.ChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Chart", description = "음악 차트 관련 API")
@RestController
@RequestMapping("/charts")
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;
    private final ChartEntryService chartEntryService;

    // 차트 목록 조회

    // 전체 차트 목록
    @Operation(summary = "전체 차트 목록", description = "모든 활성화된 차트들을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<ChartListResponseDto>> getAllCharts() {
        ChartListResponseDto responseDto = chartService.getAllActiveCharts();
        return ResponseEntity.ok(ApiResponse.success("차트 목록 조회 완료", responseDto));
    }

    @Operation(summary = "장르별 차트 조회", description = "특정 장르의 차트들을 조회합니다.")
    @GetMapping("/genre/{genre}")
    public ResponseEntity<ApiResponse<ChartListResponseDto>> getChartsByGenre(
        @Parameter(description = "장르") @PathVariable Genre genre) {

        ChartListResponseDto response = chartService.getChartsByGenre(genre);
        return ResponseEntity.ok(ApiResponse.success("장르별 차트 조회 완료", response));
    }

    @Operation(summary = "K-Pop 차트 목록", description = "K-Pop 차트들을 조회합니다. (빠른 접근)")
    @GetMapping("/kpop")
    public ResponseEntity<ApiResponse<ChartListResponseDto>> getKPopCharts() {
        ChartListResponseDto responseDto = chartService.getKPopCharts();
        return ResponseEntity.ok(ApiResponse.success("K-Pop 차트 조회 완료", responseDto));
    }

    @Operation(summary = "J-Pop 차트 목록", description = "J-Pop 차트들을 조회합니다. (빠른 접근)")
    @GetMapping("/jpop")
    public ResponseEntity<ApiResponse<ChartListResponseDto>> getJPopCharts() {
        ChartListResponseDto responseDto = chartService.getJPopCharts();
        return ResponseEntity.ok(ApiResponse.success("J-Pop 차트 조회 완료", responseDto));
    }

    @Operation(summary = "차트 최신 순위", description = "특정 차트의 최신 순위를 조회합니다.")
    @GetMapping("/{chartId}/latest")
    public ResponseEntity<ApiResponse<ChartRankingResponseDto>> getLatestRanking(
        @Parameter(description = "차트 ID") @PathVariable Long chartId,
        @Parameter(description = "Top N개만 조회 (선택사항)") @RequestBody(required = false) Integer topN
    ) {
            ChartRankingResponseDto responseDto = chartEntryService.getLatestChartRanking(chartId, topN);
            return ResponseEntity.ok(ApiResponse.success("최신 차트 순위 조회 완료", responseDto));
    }

}
