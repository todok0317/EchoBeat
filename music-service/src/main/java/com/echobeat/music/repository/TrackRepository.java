package com.echobeat.music.repository;

import com.echobeat.music.entity.Track;
import com.echobeat.music.enums.Genre;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findById (Long id);
    
    default Track findByIdOrElseThrow (Long trackId) {
        Track track = findById(trackId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 트랙입니다. id: " + trackId));
        return track;
    }

    // 제목으로 검색 (대소문자 무시)
    List<Track> findByTitleContainingIgnoreCase(String title);

    // 아티스트명으로 검색
    List<Track> findByArtistNameContainingIgnoreCase(String artistName);

    // 장르별 곡 조회
    List<Track> findByGenreOrderByCreatedAtDesc(Genre genre);

    // 제목 + 아티스트로 중복 체크 (크롤링할 때 사용)
    Optional<Track> findByTitleAndArtistName(String title, String artistName);

    // 발매일 범위로 검색
    List<Track> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);

    // Apple Music ID로 찾기
    Optional<Track> findByAppleMusicId(String appleMusicId);

    // Spotify ID로 찾기
    Optional<Track> findBySpotifyId(String spotifyId);

    // 최신 곡들 조회 (페이징)
    @Query("SELECT t FROM Track t WHERE t.genre = :genre ORDER BY t.createdAt DESC")
    List<Track> findRecentTracksByGenre(@Param("genre") Genre genre,
        org.springframework.data.domain.Pageable pageable);
}
