package com.echobeat.music.service;

import com.echobeat.music.dto.response.TrackDetailResponseDto;
import com.echobeat.music.dto.response.TrackListResponseDto;
import com.echobeat.music.dto.response.TrackResponseDto;
import com.echobeat.music.entity.Track;
import com.echobeat.music.enums.Genre;
import com.echobeat.music.repository.TrackRepository;
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


}
