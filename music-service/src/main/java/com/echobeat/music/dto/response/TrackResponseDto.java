package com.echobeat.music.dto.response;

import com.echobeat.music.entity.Track;
import com.echobeat.music.enums.Genre;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrackResponseDto {
    private Long id;
    private String title;
    private String artistName;
    private String albumName;
    private String thumbnailUrl;
    private String previewUrl;
    private Genre genre;
    private LocalDate releaseDate;
    private Integer durationMs;
    private LocalDateTime createdAt;

    public static TrackResponseDto from(Track track) {
        return TrackResponseDto.builder()
            .id(track.getId())
            .title(track.getTitle())
            .artistName(track.getArtistName())
            .albumName(track.getAlbumName())
            .thumbnailUrl(track.getThumbnailUrl())
            .previewUrl(track.getPreviewUrl())
            .genre(track.getGenre())
            .releaseDate(track.getReleaseDate())
            .durationMs(track.getDurationMs())
            .createdAt(track.getCreatedAt())
            .build();
    }
}
