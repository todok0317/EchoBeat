package com.echobeat.music.controller;

import com.echobeat.music.dto.response.TrackDetailResponseDto;
import com.echobeat.music.dto.response.TrackListResponseDto;
import com.echobeat.music.enums.Genre;
import com.echobeat.music.service.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Track", description = "음악 트랙 관련 API")
@RestController
@RequestMapping("/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    // 트랙 상세 조회
    @Operation(summary = "트랙 상세 조회", description = "트랙 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{trackId}")
    public ResponseEntity<TrackDetailResponseDto>  getTrackDetail(
        @Parameter(description = "트랙 ID") @PathVariable Long trackId) {

        TrackDetailResponseDto response = trackService.getTrackDetail(trackId);
        return ResponseEntity.ok(response);
    }

    // 장르별 트랙 조회
    @Operation(summary = "장르별 트랙 조회", description = "특정 장르의 최신 트랙들을 페이징으로 조회합니다.")
    @GetMapping("/genre/{genre}")
    public ResponseEntity<TrackListResponseDto> getTracksByGenre(
        @Parameter(description = "장르") @PathVariable Genre genre,
        @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "30") int size
    ) {
        TrackListResponseDto responseDto = trackService.getTracksByGenre(genre, page, size);
        return ResponseEntity.ok(responseDto);
    }

}
