package com.echobeat.music.repository;

import com.echobeat.music.entity.Chart;
import com.echobeat.music.enums.ChartSource;
import com.echobeat.music.enums.Country;
import com.echobeat.music.enums.Genre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartRepository extends JpaRepository<Chart, Long> {
    // ID로 조회 + 예외처리
    default Chart findByIdOrElseThrow(Long chartId) {
        return findById(chartId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 차트입니다. id: " + chartId));
    }

    // 차트명으로 찾기
    Optional<Chart> findByName(String name);

    // 활성화된 차트만 조회
    List<Chart> findByIsActiveTrueOrderByNameAsc();

    List<Chart> findByGenreAndIsActiveTrueOrderByNameAsc(Genre genre);
    
    // 소스별 조회
    List<Chart> findBySourceOrderByNameAsc(ChartSource source);
    
    // 국가별 조회
    List<Chart> findByCountryOrderByNameAsc(Country country);
    
    // 소스 + 장르로 찾기
    Optional<Chart> findBySourceAndGenre(ChartSource source, Genre genre);
    
    // 크롤링 가능한 차트들
    @Query("SELECT c FROM Chart c WHERE c.isActive = true AND c.crawlUrl IS NOT NULL ORDER BY c.name")
    List<Chart> findCrawlableCharts();

}
