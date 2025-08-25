package com.echobeat.music.controller;

import com.echobeat.common.dto.ApiResponse;
import com.echobeat.music.dto.response.ChartListResponseDto;
import com.echobeat.music.enums.Genre;
import com.echobeat.music.service.ChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Chart", description = "음악 차트 관련 API")
@RestController
@RequestMapping("/charts")
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

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

}
