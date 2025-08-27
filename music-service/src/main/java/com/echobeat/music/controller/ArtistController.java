package com.echobeat.music.controller;

import com.echobeat.common.dto.ApiResponse;
import com.echobeat.music.dto.request.ArtistRequestDto;
import com.echobeat.music.dto.request.ArtistSearchRequestDto;
import com.echobeat.music.dto.response.ArtistResponseDto;
import com.echobeat.music.dto.response.ArtistSummaryResponseDto;
import com.echobeat.music.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    @Operation(summary = "아티스트 생성", description = "아티스트를 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<ArtistResponseDto>> createArtist(
        @RequestBody ArtistRequestDto requestDto
    ) {
        ArtistResponseDto responseDto = artistService.createArtist(requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @Operation(summary = "아티스트 Id로 조회", description = "아티스트를 Id로 조회합니다.")
    @GetMapping("/{artistId}")
    public ResponseEntity<ApiResponse<ArtistResponseDto>> getArtistById(
        @PathVariable Long artistId
    ) {
        ArtistResponseDto responseDto = artistService.getArtistById(artistId);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ArtistSummaryResponseDto>>> searchArtists (
        @RequestParam(required = false) ArtistSearchRequestDto requestDto
    ) {
        Page<ArtistSummaryResponseDto> responseDtoPage = artistService.searchArtists(requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDtoPage));
    }


}
