package com.echobeat.music.crawler;

import com.echobeat.music.dto.crawler.CrawledTrackDto;
import com.echobeat.music.entity.Chart;
import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.enums.Genre;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OriconCrawler extends BaseCrawler {

    private static final String ORICON_WEEKLY_URL = "https://www.oricon.co.jp/rank/js/w/";

    public OriconCrawler(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public ChartSource getChartSource() {
        return ChartSource.ORICON;
    }

    @Override
    public List<CrawledTrackDto> crawlChart(Chart chart) {
        log.info("Oricon 주간 차트 크롤링 시작");
        List<CrawledTrackDto> tracks = new ArrayList<>();

        try {
            String html = safeHttpGet(ORICON_WEEKLY_URL);
            if (html == null) {
                log.error("Oricon HTML 조회 실패");
                return tracks;
            }

            Document doc = Jsoup.parse(html);
            Elements rankingItems = doc.select(".box-rank-entry");

            for (Element item : rankingItems) {
                try {
                    CrawledTrackDto track = parseOriconTrack(item);
                    if (track != null) {
                        tracks.add(track);
                    }
                } catch (Exception e) {
                    log.warn("Oricon 트랙 파싱 실패: {}", e.getMessage());
                }
            }

            log.info("Oricon 크롤링 완료: {}개 트랙", tracks.size());

        } catch (Exception e) {
            log.error("Oricon 크롤링 중 오류 발생", e);
        }

        return tracks;
    }

    private CrawledTrackDto parseOriconTrack(Element item) {
        try {
            // 순위
            String rankText = item.select(".num").text();
            Integer ranking = Integer.parseInt(rankText);

            // 곡 정보
            String title = cleanText(item.select(".title").text());
            String artistName = cleanText(item.select(".artist").text());

            // 썸네일
            String thumbnailUrl = item.select("img").attr("src");
            if (thumbnailUrl.startsWith("//")) {
                thumbnailUrl = "https:" + thumbnailUrl;
            }

            return CrawledTrackDto.builder()
                .title(title)
                .artistName(artistName)
                .thumbnailUrl(thumbnailUrl)
                .ranking(ranking)
                .genre(Genre.JPOP)
                .build();

        } catch (Exception e) {
            log.warn("Oricon 트랙 파싱 실패", e);
            return null;
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            String html = safeHttpGet(ORICON_WEEKLY_URL);
            return html != null && html.contains("box-rank-entry");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getCrawlerIntervalSeconds() {
        return 604800; // 7일마다 (주간 차트)
    }
}