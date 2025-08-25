package com.echobeat.music.dto.response;

import com.echobeat.music.entity.ChartEntry;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChartEntryResponseDto {
    private Long id;
    private TrackResponseDto track;
    private Integer ranking;
    private Integer previousRanking;
    private String rankingChange; // "UP", "DOWN", "SAME", "NEW"
    private Integer rankingDiff;  // 순위 변동폭
    private LocalDate chartDate;
    private Long playCount;
    private Boolean isNewEntry;

    public static ChartEntryResponseDto from(ChartEntry entry) {
        String rankingChange;
        Integer rankingDiff = null;

        if (entry.getPreviousRanking() == null) {
            rankingChange = "NEW";
        } else if (entry.getRanking() < entry.getPreviousRanking()) {
            rankingChange = "UP";
            rankingDiff = entry.getPreviousRanking() - entry.getRanking();
        } else if (entry.getRanking() > entry.getPreviousRanking()) {
            rankingChange = "DOWN";
            rankingDiff = entry.getRanking() - entry.getPreviousRanking();
        } else {
            rankingChange = "SAME";
            rankingDiff = 0;
        }

        return ChartEntryResponseDto.builder()
            .id(entry.getId())
            .track(TrackResponseDto.from(entry.getTrack()))
            .ranking(entry.getRanking())
            .previousRanking(entry.getPreviousRanking())
            .rankingChange(rankingChange)
            .rankingDiff(rankingDiff)
            .chartDate(entry.getChartDate())
            .playCount(entry.getPlayCount())
            .isNewEntry(entry.getIsNewEntry())
            .build();
    }
}
