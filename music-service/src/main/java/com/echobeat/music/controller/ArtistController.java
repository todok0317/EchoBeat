package com.echobeat.music.controller;

import com.echobeat.common.dto.ApiResponse;
import com.echobeat.music.dto.request.ArtistRequestDto;
import com.echobeat.music.dto.response.ArtistResponseDto;
import com.echobeat.music.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
