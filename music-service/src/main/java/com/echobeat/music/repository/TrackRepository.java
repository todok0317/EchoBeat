package com.echobeat.music.repository;

import com.echobeat.music.enums.Genre;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.echobeat.music.entity.Track;
import org.springframework.data.repository.query.Param;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findById (Long id);
    
    default Track findByIdOrElseThrow (Long trackId) {
        Track track = findById(trackId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 트랙입니다. id: " + trackId));
        return track;
    }

    // 장르별 곡 조회
    Page<Track> findByGenreOrderByCreatedAtDesc(Genre genre, Pageable pageable);

    // 발매일 범위 조회
    List<Track> findByReleaseDateBetweenOrderByReleaseDateDesc(
        LocalDate startDate, LocalDate endDate);

    // 최신 곡들 조회 (페이징)
    @Query("SELECT t FROM Track t WHERE t.genre = :genre ORDER BY t.createdAt DESC")
    List<Track> findRecentTracksByGenre(@Param("genre") Genre genre,
        org.springframework.data.domain.Pageable pageable);

    // 전체 검색 (제목 + 아티스트)
    @Query("SELECT t FROM Track t WHERE " +
        "LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(t.artistName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Track> searchTracks(@Param("keyword") String keyword, Pageable pageable);
    
    // 제목과 아티스트명으로 트랙 찾기
    Optional<Track> findByTitleAndArtistName(String title, String artistName);
    
    // Apple Music ID로 트랙 찾기
    Optional<Track> findByAppleMusicId(String appleMusicId);
}
