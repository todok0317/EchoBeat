package com.echobeat.music.repository;

import com.echobeat.music.entity.Artist;
import com.echobeat.music.enums.ArtistType;
import com.echobeat.music.enums.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    // 이름으로 검색 (영문, 한글, 일본어 포함)
    @Query("SELECT a FROM Artist a WHERE " +
        "a.name LIKE %:keyword% OR " +
        "a.koreanName LIKE %:keyword% OR " +
        "a.japaneseName LIKE %:keyword%")
    Page<Artist> findByNameContaining(@Param("keyword") String keyword, Pageable pageable);

    // 정확한 이름으로 찾기 (중복 체크용)
    @Query("SELECT a FROM Artist a WHERE " +
        "a.name = :name OR " +
        "a.koreanName = :name OR " +
        "a.japaneseName = :name")
    Optional<Artist> findByAnyName(@Param("name") String name);

    // 국가별 조회
    Page<Artist> findByCountry(Country country, Pageable pageable);

    // 아티스트 타입별 조회
    Page<Artist> findByArtistType(ArtistType artistType, Pageable pageable);

    // 활성 아티스트만 조회
    Page<Artist> findByIsActiveTrue(Pageable pageable);

    // 국가 + 활성 상태로 조회
    Page<Artist> findByCountryAndIsActiveTrue(Country country, Pageable pageable);

    // 소속사로 검색
    Page<Artist> findByAgencyContaining(String agency, Pageable pageable);

    // 데뷔년도 범위로 검색
    @Query("SELECT a FROM Artist a WHERE YEAR(a.debutDate) BETWEEN :startYear AND :endYear")
    List<Artist> findByDebutYearBetween(@Param("startYear") int startYear,
        @Param("endYear") int endYear);

    // 인기순 조회 (트랙 개수 기준)
    @Query("SELECT a FROM Artist a LEFT JOIN a.trackArtists ta " +
        "GROUP BY a.id " +
        "ORDER BY COUNT(ta.id) DESC")
    Page<Artist> findByPopularity(Pageable pageable);

    // 최근 데뷔 아티스트
    Page<Artist> findByIsActiveTrueOrderByDebutDateDesc(Pageable pageable);

    // 이름 존재 여부 확인
    boolean existsByName(String name);
    boolean existsByKoreanName(String koreanName);
    boolean existsByJapaneseName(String japaneseName);
}