package com.echobeat.music.service;

import com.echobeat.music.dto.response.TrackDetailResponseDto;
import com.echobeat.music.entity.Track;
import com.echobeat.music.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


}
