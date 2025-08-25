package com.echobeat.music.repository;

import com.echobeat.music.entity.Chart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartRepository extends JpaRepository<Chart, Long> {
    // ID로 조회 + 예외처리
    default Chart findByIdOrElseThrow(Long chartId) {
        return findById(chartId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 차트입니다. id: " + chartId));
    }

    // 활성화된 차트만 조회
    List<Chart> findByIsActiveTrueOrderByNameAsc();


}
