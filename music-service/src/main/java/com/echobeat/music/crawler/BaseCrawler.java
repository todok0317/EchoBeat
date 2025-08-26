package com.echobeat.music.crawler;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public abstract class BaseCrawler implements ChartCrawler {

    protected final RestTemplate restTemplate;

    @Value("${crawler.user-agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36}")
    protected String userAgent;

    @Value("${crawler.delay-ms:1000}")
    protected long delayMs;

    public BaseCrawler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 크롤링 간 대기
    protected void delay() {
        try {
            TimeUnit.MILLISECONDS.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Crawler delay interrupted", e);
        }
    }

    // 안전한 HTTP 요청
    protected String safeHttpGet(String url) {
        try {
            delay();
            return restTemplate.getForObject(url, String.class);
        } catch (HttpClientErrorException e) {
            log.error("HTTP error for URL {} : {}", url, e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Unexpected error for URL {} : {}", url, e.getMessage());
            return null;
        }
    }

    // 문자열 정리
    protected String cleanText(String text) {
        if (text == null) return null;
        return text.trim()
            .replaceAll("\\s+", " ")
            .replaceAll("[\r\n\t]", "");
    }
}
