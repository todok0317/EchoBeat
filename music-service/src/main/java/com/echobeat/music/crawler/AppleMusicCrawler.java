package com.echobeat.music.crawler;

import com.echobeat.music.dto.crawler.CrawledTrackDto;
import com.echobeat.music.entity.Chart;
import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.enums.Genre;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AppleMusicCrawler extends BaseCrawler {

    private static final String APPLE_MUSIC_API_URL = "https://api.music.apple.com/v1/catalog";

    @Value("${apple.music.api.key:}")
    private String apiKey;

    private final ObjectMapper objectMapper;

    public AppleMusicCrawler(RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(restTemplate);
        this.objectMapper = objectMapper;
    }


    @Override
    public ChartSource getChartSource() {
        return ChartSource.APPLE_MUSIC;
    }

    @Override
    public List<CrawledTrackDto> crawlChart(Chart chart) {
        log.info("Apple Music 차트 크롤링 시작: {}", chart.getName());
        List<CrawledTrackDto> tracks = new ArrayList<>();

        if (apiKey.isEmpty()) {
            log.warn("Apple Music API 키가 설정되지 않음");
            return tracks;
        }

        try {
            String countryCode = getCountryCode(chart.getGenre());
            String url = String.format("%s/%s/charts?types=songs&limit=100", APPLE_MUSIC_API_URL, countryCode);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Music-User-Token", ""); // 필요시 사용자 토큰

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                tracks = parseAppleMusicResponse(response.getBody(), chart.getGenre());
            }

            log.info("Apple Music 크롤링 완료: {}개 트랙", tracks.size());

        } catch (Exception e) {
            log.error("Apple Music 크롤링 중 오류 발생", e);
        }

        return tracks;
    }

    private String getCountryCode(Genre genre) {
        return switch (genre) {
            case KPOP -> "kr";
            case JPOP -> "jp";
            default -> "us";
        };
    }

    private List<CrawledTrackDto> parseAppleMusicResponse(String jsonResponse, Genre genre) {
        List<CrawledTrackDto> tracks = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode songsChart = root.path("results").path("songs").get(0);
            JsonNode data = songsChart.path("data");

            int ranking = 1;
            for (JsonNode item : data) {
                JsonNode attributes = item.path("attributes");

                String title = cleanText(attributes.path("name").asText());
                String artistName = cleanText(attributes.path("artistName").asText());
                String albumName = cleanText(attributes.path("albumName").asText());

                // 썸네일 URL
                String thumbnailUrl = attributes.path("artwork").path("url").asText()
                    .replace("{w}", "300").replace("{h}", "300");

                // 미리듣기 URL
                String previewUrl = attributes.path("previews").get(0).path("url").asText();

                // Apple Music ID
                String appleMusicId = item.path("id").asText();

                CrawledTrackDto track = CrawledTrackDto.builder()
                    .title(title)
                    .artistName(artistName)
                    .albumName(albumName)
                    .thumbnailUrl(thumbnailUrl)
                    .previewUrl(previewUrl)
                    .ranking(ranking++)
                    .genre(genre)
                    .appleMusicId(appleMusicId)
                    .build();

                tracks.add(track);
            }

        } catch (Exception e) {
            log.error("Apple Music 응답 파싱 실패", e);
        }

        return tracks;
    }

    @Override
    public boolean isAvailable() {
        return !apiKey.isEmpty();
    }

    @Override
    public int getCrawlerIntervalSeconds() {
        return 3600;
    }

}