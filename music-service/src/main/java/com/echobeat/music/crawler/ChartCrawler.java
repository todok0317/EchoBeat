package com.echobeat.music.crawler;

import com.echobeat.music.dto.crawler.CrawledTrackDto;
import com.echobeat.music.entity.Chart;
import com.echobeat.music.enums.ChartSource;
import java.util.List;

public interface ChartCrawler {

    // 지원하는 차트 소스
    ChartSource getChartSource();

    // 크롤링 실행
    List<CrawledTrackDto> crawlChart(Chart chart);

    // 크롤러 사용 가능 여부 체크
    boolean isAvailable();

    // 크롤링 간격 (초)
    int getCrawlerIntervalSeconds();

}
