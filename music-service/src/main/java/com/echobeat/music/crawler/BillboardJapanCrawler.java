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
public class BillboardJapanCrawler extends BaseCrawler {

    private static final String BILLBOARD_JAPAN_HOT100_URL = "https://www.billboard-japan.com/charts/detail?a=hot100";

    public BillboardJapanCrawler(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public ChartSource getChartSource() {
        return ChartSource.BILLBOARD_JAPAN;
    }

    @Override
    public List<CrawledTrackDto> crawlChart(Chart chart) {
        log.info("Billboard Japan Hot 100 크롤링 시작");
        List<CrawledTrackDto> tracks = new ArrayList<>();

        try {
            String html = safeHttpGet(BILLBOARD_JAPAN_HOT100_URL);
            if (html == null) {
                log.error("Billboard Japan HTML 조회 실패");
                return tracks;
            }

            Document doc = Jsoup.parse(html);
            Elements chartEntries = doc.select(".chart_entry");

            for (Element entry : chartEntries) {
                try {
                    CrawledTrackDto track = parseBillboardTrack(entry);
                    if (track != null) {
                        tracks.add(track);
                    }
                } catch (Exception e) {
                    log.warn("Billboard Japan 트랙 파싱 실패: {}", e.getMessage());
                }
            }

            log.info("Billboard Japan 크롤링 완료: {}개 트랙", tracks.size());

        } catch (Exception e) {
            log.error("Billboard Japan 크롤링 중 오류 발생", e);
        }

        return tracks;
    }

    private CrawledTrackDto parseBillboardTrack(Element entry) {
        try {
            // 순위
            String rankText = entry.select(".chart_position").text();
            Integer ranking = Integer.parseInt(rankText.trim());

            // 곡 정보
            Element songInfo = entry.select(".chart_data").first();
            String title = cleanText(songInfo.select(".chart_title").text());
            String artistName = cleanText(songInfo.select(".chart_artist").text());

            // 썸네일
            String thumbnailUrl = entry.select("img.chart_image").attr("src");

            return CrawledTrackDto.builder()
                .title(title)
                .artistName(artistName)
                .thumbnailUrl(thumbnailUrl)
                .ranking(ranking)
                .genre(Genre.JPOP)
                .build();

        } catch (Exception e) {
            log.warn("Billboard Japan 트랙 파싱 실패", e);
            return null;
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            String html = safeHttpGet(BILLBOARD_JAPAN_HOT100_URL);
            return html != null && html.contains("chart_entry");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getCrawlerIntervalSeconds() {
        return 86400;
    }
}