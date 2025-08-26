package com.echobeat.music.crawler;

import com.echobeat.music.dto.crawler.CrawledTrackDto;
import com.echobeat.music.entity.Chart;
import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.enums.Genre;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class MelonCrawler extends BaseCrawler{

    private static final String MELON_REALTIME_URL = "https://www.melon.com/chart/index.htm";

    public MelonCrawler(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public ChartSource getChartSource() {
        return ChartSource.MELON;
    }

    @Override
    public List<CrawledTrackDto> crawlChart(Chart chart) {
        log.info("멜론 실시간 차트 크롤링 시작");
        List<CrawledTrackDto> tracks = new ArrayList<>();

        try {
            String html = safeHttpGet(MELON_REALTIME_URL);
            if(html == null) {
                log.error("멜론 차트 HTML 조회 실패");
                return tracks;
            }

            Document document = Jsoup.parse(html);
            Elements trackElements = document.select("tr.lst50, tr.lst100");

            for (Element element : trackElements) {
                try {
                    CrawledTrackDto track = parseMelonTrack(element);
                    if (track != null) {
                        tracks.add(track);
                    }
                } catch (Exception e) {
                    log.warn("멜론 트랙 파싱 실패: {}", e.getMessage());
                }
            }

            log.info("멜론 크롤링 완료: {}개 트랙", tracks.size());
        } catch (Exception e) {
            log.error("멜론 크롤링 중 오류 발생", e);
        }
        return tracks;
    }

    private CrawledTrackDto parseMelonTrack(Element element) {
        try {
            // 순위
            String rankText = element.select(".rank").text();
            Integer ranking = Integer.parseInt(rankText.replaceAll("[^0-9]", ""));

            // 곡 정보
            Element songInfo = element.select(".wrap_song_info").first();
            String title = cleanText(songInfo.select(".rank01 a").text());
            String artistName = cleanText(songInfo.select(".rank02 a").text());
            String albumName = cleanText(songInfo.select(".rank03 a").text());

            // 썸네일
            String thumbnailUrl = element.select("img").attr("src");

            // 재생수 (멜론은 공개하지 않으므로 null)

            return CrawledTrackDto.builder()
                .title(title)
                .artistName(artistName)
                .albumName(albumName)
                .thumbnailUrl(thumbnailUrl)
                .ranking(ranking)
                .genre(Genre.KPOP)
                .build();

        } catch (Exception e) {
            log.warn("멜론 트랙 파싱 실패", e);
            return null;
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            String html = safeHttpGet(MELON_REALTIME_URL);
            return html != null && html.contains("chart");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getCrawlerIntervalSeconds() {
        return 300;
    }
}
