package com.echobeat.music.service;

import com.echobeat.music.dto.request.ArtistRequestDto;
import com.echobeat.music.dto.request.ArtistSearchRequestDto;
import com.echobeat.music.dto.response.ArtistResponseDto;
import com.echobeat.music.dto.response.ArtistSummaryResponseDto;
import com.echobeat.music.entity.Artist;
import com.echobeat.music.enums.ArtistType;
import com.echobeat.music.enums.Country;
import com.echobeat.music.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;

    // 아티스트 생성
    @Transactional
    public ArtistResponseDto createArtist (ArtistRequestDto artistRequestDto) {
        // 이름 영문명 중복 체크
        if(artistRepository.existsByName(artistRequestDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 아티스트명입니다 : " + artistRequestDto.getName());
        }

        // 한글명 중복 체크
        if(artistRequestDto.getKoreanName() != null &&
            !artistRequestDto.getKoreanName().trim().isEmpty() &&
            artistRepository.existsByKoreanName(artistRequestDto.getKoreanName())) {
            throw new IllegalArgumentException("이미 존재하는 한글명입니다. : " + artistRequestDto.getKoreanName());
        }

        // 일본어명 중복 체크
        if(artistRequestDto.getJapaneseName() != null &&
            !artistRequestDto.getJapaneseName().trim().isEmpty() &&
            artistRepository.existsByJapaneseName(artistRequestDto.getJapaneseName())) {
            throw new IllegalArgumentException("이미 존재하는 일본어명입니다. : " + artistRequestDto.getJapaneseName());
        }

        Artist artist = Artist.builder()
            .name(artistRequestDto.getName())
            .koreanName(artistRequestDto.getKoreanName())
            .japaneseName(artistRequestDto.getJapaneseName())
            .profileImageUrl(artistRequestDto.getProfileImageUrl())
            .country(artistRequestDto.getCountry())
            .debutDate(artistRequestDto.getDebutDate())
            .agency(artistRequestDto.getAgency())
            .memberCount(artistRequestDto.getMemberCount())
            .description(artistRequestDto.getDescription())
            .isActive(true)
            .build();

        Artist savedArtist = artistRepository.save(artist);
        log.info("새 아티스트가 생성되었습니다 : {} ({})", savedArtist.getName(), savedArtist.getId());

        return ArtistResponseDto.from(savedArtist);
    }

    // 아티스트 Id로 조회
    public ArtistResponseDto getArtistById (Long artistId) {
        Artist artist = artistRepository.findById(artistId)
            .orElseThrow(() -> new IllegalArgumentException("아티스트를 찾을 수 없습니다 : " + artistId));

        return ArtistResponseDto.from(artist);
    }

    // 아티스트 검색 (페이징)
    public Page<ArtistSummaryResponseDto> searchArtists (ArtistSearchRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
        Page<Artist> artistPage;

        if (requestDto.getKeyword() != null && !requestDto.getKeyword().trim().isEmpty()) {
            artistPage = artistRepository.findByNameContaining(requestDto.getKeyword().trim(), pageable);
        } else if (requestDto.getCountry() != null) {
            if (requestDto.getIsActive() != null && requestDto.getIsActive()) {
                artistPage = artistRepository.findByCountryAndIsActiveTrue(requestDto.getCountry(), pageable);
            } else {
                artistPage = artistRepository.findByCountry(requestDto.getCountry(), pageable);
            }
        } else if (requestDto.getArtistType() != null) {
            artistPage = artistRepository.findByArtistType(requestDto.getArtistType(), pageable);
        } else if (requestDto.getAgency() != null && !requestDto.getAgency().trim().isEmpty()) {
            artistPage = artistRepository.findByAgencyContaining(requestDto.getAgency().trim(), pageable);
        } else if (requestDto.getIsActive() != null && requestDto.getIsActive()) {
            artistPage = artistRepository.findByIsActiveTrue(pageable);
        } else {
            artistPage = artistRepository.findAll(pageable);
        }

        return artistPage.map(ArtistSummaryResponseDto::from);
    }

    // 모든 활성 아티스트 조회
    public Page<ArtistSummaryResponseDto> getAllActiveArtists (int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Artist> artistPage = artistRepository.findByIsActiveTrue(pageable);
        return artistPage.map(ArtistSummaryResponseDto::from);
    }

    // 국가별 아티스트 조회
    public Page<ArtistSummaryResponseDto> getArtistsByCountry(Country country, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Artist> artistPage = artistRepository.findByCountryAndIsActiveTrue(country, pageable);
        return artistPage.map(ArtistSummaryResponseDto::from);
    }

    // 아티스트 타입별 조회
    public Page<ArtistSummaryResponseDto> getArtistsByArtistType(ArtistType artistType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Artist> artistPage = artistRepository.findByArtistType(artistType, pageable);
        return artistPage.map(ArtistSummaryResponseDto::from);
    }

    // 인기 아티스트 조회
    public Page<ArtistSummaryResponseDto> getPopularArtists(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Artist> artistPage = artistRepository.findByPopularity(pageable);
        return artistPage.map(ArtistSummaryResponseDto::from);
    }





}
