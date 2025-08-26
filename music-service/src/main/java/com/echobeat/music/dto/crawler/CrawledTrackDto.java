package com.echobeat.music.dto.crawler;

import com.echobeat.music.enums.Genre;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CrawledTrackDto {

    private String title;

    private String artistName;

    private String albumName;

    private String thumbnailUrl;

    private String previewUrl;

    private Integer ranking;

    private Long playCount;

    private Long likeCount;

    private Genre genre;

    // 외부 ID들
    private String spotifyId;

    private String appleMusicId;

    private String youtubeId;
}
