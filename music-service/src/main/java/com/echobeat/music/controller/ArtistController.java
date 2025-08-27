package com.echobeat.music.controller;

import com.echobeat.common.dto.ApiResponse;
import com.echobeat.music.dto.request.ArtistRequestDto;
import com.echobeat.music.dto.request.ArtistSearchRequestDto;
import com.echobeat.music.dto.request.ArtistUpdateRequestDto;
import com.echobeat.music.dto.response.ArtistResponseDto;
import com.echobeat.music.dto.response.ArtistSummaryResponseDto;
import com.echobeat.music.enums.ArtistType;
import com.echobeat.music.enums.Country;
import com.echobeat.music.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    // 아티스트 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ArtistSummaryResponseDto>>> searchArtists (
        @RequestParam(required = false) ArtistSearchRequestDto requestDto
    ) {
        Page<ArtistSummaryResponseDto> responseDtoPage = artistService.searchArtists(requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDtoPage));
    }

    // 모든 활성 아티스트 조회
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<ArtistSummaryResponseDto>>> getAllActiveArtists (
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        Page<ArtistSummaryResponseDto> responseDtoPage = artistService.getAllActiveArtists(page, size);
        return ResponseEntity.ok(ApiResponse.success(responseDtoPage));
    }

    // 국가별 아티스트 조회
    @GetMapping("/country")
    public ResponseEntity<ApiResponse<Page<ArtistSummaryResponseDto>>> getArtistsByCountry (
        @RequestParam String country,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ){
        Country countryEnum = Country.valueOf(country.toUpperCase());
        Page<ArtistSummaryResponseDto> responseDtoPage = artistService.getArtistsByCountry(countryEnum, page, size);
        return ResponseEntity.ok(ApiResponse.success(responseDtoPage));
    }

    // 아티스트 타입별 조회
    @GetMapping("/type")
    public ResponseEntity<ApiResponse<Page<ArtistSummaryResponseDto>>> getArtistsByType(
        @RequestParam String type,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ){
        ArtistType typeEnum = ArtistType.valueOf(type.toUpperCase());
        Page<ArtistSummaryResponseDto> responseDtoPage = artistService.getArtistsByArtistType(typeEnum, page, size);
        return ResponseEntity.ok(ApiResponse.success(responseDtoPage));
    }

    // 인기 아티스트 조회 (트랙 개수 기준)
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Page<ArtistSummaryResponseDto>>> getPopularArtists (
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ) {
        Page<ArtistSummaryResponseDto> responseDtoPage = artistService.getPopularArtists(page, size);
        return ResponseEntity.ok(ApiResponse.success(responseDtoPage));
    }

    // 최근 데뷔 아티스트 조회
    @GetMapping("/debut")
    public ResponseEntity<ApiResponse<Page<ArtistSummaryResponseDto>>> getRecentDebutArtists (
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size
    ){
        Page<ArtistSummaryResponseDto> responseDtoPage = artistService.getRecentDebutArtists(page, size);
        return ResponseEntity.ok(ApiResponse.success(responseDtoPage));
    }

    // 아티스트 정보 수정
    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<ArtistResponseDto>> updateArtist(
        @RequestParam Long artistId,
        @RequestBody ArtistUpdateRequestDto requestDto
    ) {
        ArtistResponseDto responseDto = artistService.updateArtist(artistId, requestDto);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    // 아티스트 비활성화
    @PatchMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> disableArtist(
        @RequestParam Long artistId
    ){
        artistService.disableArtist(artistId);
        return ResponseEntity.ok(ApiResponse.success("비활성화 되었습니다."));
    }

    // 아티스트 활성화
    @PatchMapping("/activate")
    public ResponseEntity<ApiResponse<Void>> activateArtist(
        @RequestParam Long artistId
    ){
        artistService.activateArtist(artistId);
        return ResponseEntity.ok(ApiResponse.success("활성화 되었습니다."));
    }

}
