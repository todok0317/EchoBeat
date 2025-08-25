package com.echobeat.music.service;

import com.echobeat.music.dto.request.TrackSearchRequestDto;
import com.echobeat.music.dto.response.TrackDetailResponseDto;
import com.echobeat.music.dto.response.TrackListResponseDto;
import com.echobeat.music.dto.response.TrackResponseDto;
import com.echobeat.music.entity.Track;
import com.echobeat.music.enums.Genre;
import com.echobeat.music.repository.TrackRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    // 트랙 ID로 상세 조회
    public TrackDetailResponseDto getTrackDetail(Long trackId) {
        Track track = trackRepository.findByIdOrElseThrow(trackId);
        return TrackDetailResponseDto.from(track);
    }

    // 장르별 최신 트랙 조회
    public TrackListResponseDto getTracksByGenre(Genre genre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Track> trackPage = trackRepository.findByGenreOrderByCreatedAtDesc(genre, pageable);

        List<TrackResponseDto> tracks = trackPage.getContent().stream()
            .map(TrackResponseDto::from)
            .collect(Collectors.toList());

        return TrackListResponseDto.builder()
            .tracks(tracks)
            .totalElements(trackPage.getTotalElements())
            .totalPages(trackPage.getTotalPages())
            .currentPage(trackPage.getNumber())
            .hasNext(trackPage.hasNext())
            .build();
    }

    // 트랙 검색 (제목 + 아티스트)
    public TrackListResponseDto searchTracks(TrackSearchRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());

        List<Track> tracks;
        if(requestDto.getKeyword() != null && !requestDto.getKeyword().trim().isEmpty()) {
            tracks = trackRepository.searchTracks(requestDto.getKeyword().trim(), pageable);
        } else {
            // 키워드가 없으면 장르별 최신곡
            tracks = trackRepository.findRecentTracksByGenre(
                requestDto.getGenre() != null ? requestDto.getGenre() : Genre.KPOP, pageable
            );
        }

        List<TrackResponseDto> trackDtos = tracks.stream()
            .map(TrackResponseDto::from)
            .collect(Collectors.toList());

        return TrackListResponseDto.builder()
            .tracks(trackDtos)
            .totalElements((long) tracks.size())
            .currentPage(requestDto.getPage())
            .build();
    }

    // 발매일 범위별 트랙 조회
    public List<TrackResponseDto> getTracksByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Track> tracks = trackRepository.findByReleaseDateBetweenOrderByReleaseDateDesc(startDate, endDate);

        return tracks.stream()
            .map(TrackResponseDto::from)
            .collect(Collectors.toList());
    }

    public List<TrackResponseDto> getTrendingTracks(Genre genre, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Track> tracks = trackRepository.findRecentTracksByGenre(genre, pageable);

        return tracks.stream()
            .map(TrackResponseDto::from)
            .collect(Collectors.toList());
    }

}
