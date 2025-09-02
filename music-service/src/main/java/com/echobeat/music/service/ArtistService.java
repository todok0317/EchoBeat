package com.echobeat.music.service;

import com.echobeat.music.dto.request.ArtistRequestDto;
import com.echobeat.music.dto.request.ArtistSearchRequestDto;
import com.echobeat.music.dto.request.ArtistUpdateRequestDto;
import com.echobeat.music.dto.response.ArtistResponseDto;
import com.echobeat.music.dto.response.ArtistSummaryResponseDto;
import com.echobeat.music.entity.Artist;
import com.echobeat.music.enums.ArtistType;
import com.echobeat.music.enums.Country;
import com.echobeat.music.repository.ArtistRepository;
import java.time.LocalDate;
import java.util.Optional;
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

    // 최근 데뷔 아티스트 조회
    public Page<ArtistSummaryResponseDto> getRecentDebutArtists(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Artist> artistPage = artistRepository.findByIsActiveTrueOrderByDebutDateDesc(pageable);
        return artistPage.map(ArtistSummaryResponseDto::from);
    }

    // 아티스트 정보 수정
    @Transactional
    public ArtistResponseDto updateArtist(Long artistId, ArtistUpdateRequestDto requestDto) {
        Artist artist = artistRepository.findById(artistId)
            .orElseThrow(() -> new IllegalArgumentException("아티스트를 찾을 수 없습니다 : " + artistId));

        artist.updateProfile(
            requestDto.getKoreanName(),
            requestDto.getJapaneseName(),
            requestDto.getProfileImageUrl(),
            requestDto.getAgency(),
            requestDto.getDescription()
        );
        log.info("아티스트 정보가 수정되었습니다 : {} ({})", artist.getName(), artist.getId());
        return ArtistResponseDto.from(artist);
    }

    // 아티스트 비활성화
    @Transactional
    public void disableArtist(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
            .orElseThrow(() -> new IllegalArgumentException("아티스트를 찾을 수 없습니다 : " + artistId));

        artist.deactivate();
        log.info("아티스트가 비활성화되었습니다 : {} ({})", artist.getName(), artist.getId());
    }

    // 아티스트 활성화
    @Transactional
    public void activateArtist(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
            .orElseThrow(() -> new IllegalArgumentException("아티스트를 찾을 수 없습니다 : " + artistId));
        artist.activate();
        log.info("아티스트가 활성화되었습니다: {} ({})", artist.getName(), artistId);
    }

    // 크롤링용 아티스트 생성 또는 조회
    // 아티스트명으로 검색 후, 없으면 기본 정보로 생성
    @Transactional
    public Artist findOrCreateArtistForCrawling(String artistName, Country country) {
        // 1. 영문명으로 검색
        Optional<Artist> existingArtist = artistRepository.findByNameIgnoreCase(artistName);
        if (existingArtist.isPresent()) {
            log.info("기존 아티스트 반환: {}", artistName);
            return existingArtist.get();
        }

        // 2. 한글명/일본어명으로도 검색 (크롤링 데이터가 한글/일본어일 수 있음)
        existingArtist = artistRepository.findByKoreanNameIgnoreCase(artistName);
        if (existingArtist.isPresent()) {
            log.info("기존 아티스트 반환 (한글명 매칭): {}", artistName);
            return existingArtist.get();
        }

        existingArtist = artistRepository.findByJapaneseNameIgnoreCase(artistName);
        if (existingArtist.isPresent()) {
            log.info("기존 아티스트 반환 (일본어명 매칭): {}", artistName);
            return existingArtist.get();
        }

        // 3. 새 아티스트 생성 (크롤링용 기본 정보)
        Artist newArtist = createArtistFromCrawling(artistName, country);
        log.info("크롤링으로 새 아티스트 생성: {} ({})", newArtist.getDisplayName(), newArtist.getId());

        return newArtist;
    }

    // 크롤링 데이터로 아티스트 생성
    private Artist createArtistFromCrawling(String artistName, Country country) {
        Artist.ArtistBuilder builder = Artist.builder()
            .country(country)
            .artistType(ArtistType.UNKNOWN) // 기본값
            .isActive(true);

        // 언어별 이름 설정 로직
        if (isKorean(artistName)) {
            builder.koreanName(artistName);
            builder.name(romanizeKorean(artistName)); // 영문명 생성 로직 필요
        } else if (isJapanese(artistName)) {
            builder.japaneseName(artistName);
            builder.name(romanizeJapanese(artistName)); // 영문명 생성 로직 필요
        } else {
            builder.name(artistName);
        }

        return artistRepository.save(builder.build());
    }

    // 한글 여부 검사
    private boolean isKorean(String text) {
        return text.matches(".*[\\u3131-\\u3163\\uac00-\\ud7a3].*");
    }

    // 일본어 여부 검사
    private boolean isJapanese(String text) {
        return text.matches(".*[\\u3040-\\u309F\\u30A0-\\u30FF\\u4E00-\\u9FAF].*");
    }

    // 아티스트 정보 보완 (크롤링 후 수동 보완용)
    @Transactional
    public Artist enrichArtistInfo(Long artistId, ArtistType artistType,
        LocalDate debutDate, String agency,
        Integer memberCount, String profileImageUrl) {
        Artist artist = artistRepository.findById(artistId)
            .orElseThrow(() -> new IllegalArgumentException("아티스트를 찾을 수 없습니다: " + artistId));

        // 크롤링으로 생성된 아티스트 정보 보완
        if (artistType != null && artist.getArtistType() == ArtistType.UNKNOWN) {
            artist.setArtistType(artistType);
        }
        if (debutDate != null && artist.getDebutDate() == null) {
            artist.setDebutDate(debutDate);
        }
        if (agency != null && artist.getAgency() == null) {
            artist.setAgency(agency);
        }
        if (memberCount != null && artist.getMemberCount() == null) {
            artist.setMemberCount(memberCount);
        }
        if (profileImageUrl != null && artist.getProfileImageUrl() == null) {
            artist.setProfileImageUrl(profileImageUrl);
        }

        return artistRepository.save(artist);
    }
    
    // 크롤링 시스템을 위한 배치 아티스트 처리
    @Transactional
    public void processBatchArtists(java.util.List<String> artistNames, Country country) {
        for (String artistName : artistNames) {
            try {
                findOrCreateArtistForCrawling(artistName.trim(), country);
                log.info("배치 처리 완료: {}", artistName);
            } catch (Exception e) {
                log.error("배치 처리 실패: {} - {}", artistName, e.getMessage());
            }
        }
    }
    
    // 중복 아티스트 병합 (관리자용)
    @Transactional 
    public Artist mergeArtists(Long keepArtistId, Long deleteArtistId) {
        Artist keepArtist = artistRepository.findById(keepArtistId)
            .orElseThrow(() -> new IllegalArgumentException("유지할 아티스트를 찾을 수 없습니다: " + keepArtistId));
        
        Artist deleteArtist = artistRepository.findById(deleteArtistId)
            .orElseThrow(() -> new IllegalArgumentException("삭제할 아티스트를 찾을 수 없습니다: " + deleteArtistId));
        
        // 삭제할 아티스트의 정보를 유지할 아티스트에 병합
        if (keepArtist.getKoreanName() == null && deleteArtist.getKoreanName() != null) {
            keepArtist.updateProfile(deleteArtist.getKoreanName(), keepArtist.getJapaneseName(), 
                keepArtist.getProfileImageUrl(), keepArtist.getAgency(), keepArtist.getDescription());
        }
        if (keepArtist.getJapaneseName() == null && deleteArtist.getJapaneseName() != null) {
            keepArtist.updateProfile(keepArtist.getKoreanName(), deleteArtist.getJapaneseName(),
                keepArtist.getProfileImageUrl(), keepArtist.getAgency(), keepArtist.getDescription());
        }
        
        // TODO: 관련된 TrackArtist, Album 등의 관계도 이전해야 함
        
        // 삭제할 아티스트 비활성화
        deleteArtist.deactivate();
        
        log.info("아티스트 병합 완료: {} <- {}", keepArtist.getName(), deleteArtist.getName());
        return artistRepository.save(keepArtist);
    }
    
    // UNKNOWN 타입 아티스트들 조회 (수동 보완 대상)
    public Page<ArtistSummaryResponseDto> getUnknownTypeArtists(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Artist> artistPage = artistRepository.findByArtistType(ArtistType.UNKNOWN, pageable);
        return artistPage.map(ArtistSummaryResponseDto::from);
    }
    
    // 아티스트명 정규화 (크롤링 데이터 정제용)
    public String normalizeArtistName(String rawName) {
        if (rawName == null) return null;
        
        return rawName.trim()
            .replaceAll("\\s+", " ")  // 연속된 공백을 단일 공백으로
            .replaceAll("[\\[\\(].*?[\\]\\)]", "") // 괄호 안 내용 제거 (예: "아이유 (IU)" -> "아이유")
            .replaceAll("feat\\.|ft\\.", "") // feat. 제거
            .replaceAll("&", "and") // & -> and 변환
            .trim();
    }
    
    // 한글을 로마자로 변환 (기본적인 음성학적 변환)
    private String romanizeKorean(String korean) {
        if (korean == null || korean.trim().isEmpty()) {
            return korean;
        }
        
        // 기본적인 한글 로마자 변환 맵핑 (일부만 구현)
        String romanized = korean
            .replace("가", "ga").replace("나", "na").replace("다", "da")
            .replace("라", "ra").replace("마", "ma").replace("바", "ba")
            .replace("사", "sa").replace("아", "a").replace("자", "ja")
            .replace("차", "cha").replace("카", "ka").replace("타", "ta")
            .replace("파", "pa").replace("하", "ha")
            .replace("고", "go").replace("노", "no").replace("도", "do")
            .replace("로", "ro").replace("모", "mo").replace("보", "bo")
            .replace("소", "so").replace("오", "o").replace("조", "jo")
            .replace("초", "cho").replace("코", "ko").replace("토", "to")
            .replace("포", "po").replace("호", "ho")
            .replace("구", "gu").replace("누", "nu").replace("두", "du")
            .replace("루", "ru").replace("무", "mu").replace("부", "bu")
            .replace("수", "su").replace("우", "u").replace("주", "ju")
            .replace("추", "chu").replace("쿠", "ku").replace("투", "tu")
            .replace("푸", "pu").replace("후", "hu")
            .replace("이", "i").replace("유", "yu").replace("은", "eun")
            .replace("을", "eul").replace("의", "ui");
            
        // 변환되지 않은 한글이 있다면 원본 반환
        if (romanized.matches(".*[\\u3131-\\u3163\\uac00-\\ud7a3].*")) {
            return korean; // 완전히 변환되지 않으면 원본 유지
        }
        
        return romanized;
    }
    
    // 일본어를 로마자로 변환 (기본적인 히라가나/가타카나 변환)
    private String romanizeJapanese(String japanese) {
        if (japanese == null || japanese.trim().isEmpty()) {
            return japanese;
        }
        
        // 기본적인 일본어 로마자 변환 맵핑 (일부만 구현)
        String romanized = japanese
            // 히라가나
            .replace("あ", "a").replace("い", "i").replace("う", "u").replace("え", "e").replace("お", "o")
            .replace("か", "ka").replace("き", "ki").replace("く", "ku").replace("け", "ke").replace("こ", "ko")
            .replace("が", "ga").replace("ぎ", "gi").replace("ぐ", "gu").replace("げ", "ge").replace("ご", "go")
            .replace("さ", "sa").replace("し", "shi").replace("す", "su").replace("せ", "se").replace("そ", "so")
            .replace("ざ", "za").replace("じ", "ji").replace("ず", "zu").replace("ぜ", "ze").replace("ぞ", "zo")
            .replace("た", "ta").replace("ち", "chi").replace("つ", "tsu").replace("て", "te").replace("と", "to")
            .replace("だ", "da").replace("ぢ", "di").replace("づ", "du").replace("で", "de").replace("ど", "do")
            .replace("な", "na").replace("に", "ni").replace("ぬ", "nu").replace("ね", "ne").replace("の", "no")
            .replace("は", "ha").replace("ひ", "hi").replace("ふ", "fu").replace("へ", "he").replace("ほ", "ho")
            .replace("ば", "ba").replace("び", "bi").replace("ぶ", "bu").replace("べ", "be").replace("ぼ", "bo")
            .replace("ぱ", "pa").replace("ぴ", "pi").replace("ぷ", "pu").replace("ぺ", "pe").replace("ぽ", "po")
            .replace("ま", "ma").replace("み", "mi").replace("む", "mu").replace("め", "me").replace("も", "mo")
            .replace("や", "ya").replace("ゆ", "yu").replace("よ", "yo")
            .replace("ら", "ra").replace("り", "ri").replace("る", "ru").replace("れ", "re").replace("ろ", "ro")
            .replace("わ", "wa").replace("ゐ", "wi").replace("ゑ", "we").replace("を", "wo")
            .replace("ん", "n")
            // 가타카나
            .replace("ア", "a").replace("イ", "i").replace("ウ", "u").replace("エ", "e").replace("オ", "o")
            .replace("カ", "ka").replace("キ", "ki").replace("ク", "ku").replace("ケ", "ke").replace("コ", "ko")
            .replace("ガ", "ga").replace("ギ", "gi").replace("グ", "gu").replace("ゲ", "ge").replace("ゴ", "go")
            .replace("サ", "sa").replace("シ", "shi").replace("ス", "su").replace("セ", "se").replace("ソ", "so")
            .replace("ザ", "za").replace("ジ", "ji").replace("ズ", "zu").replace("ゼ", "ze").replace("ゾ", "zo")
            .replace("タ", "ta").replace("チ", "chi").replace("ツ", "tsu").replace("テ", "te").replace("ト", "to")
            .replace("ダ", "da").replace("ヂ", "di").replace("ヅ", "du").replace("デ", "de").replace("ド", "do")
            .replace("ナ", "na").replace("ニ", "ni").replace("ヌ", "nu").replace("ネ", "ne").replace("ノ", "no")
            .replace("ハ", "ha").replace("ヒ", "hi").replace("フ", "fu").replace("ヘ", "he").replace("ホ", "ho")
            .replace("バ", "ba").replace("ビ", "bi").replace("ブ", "bu").replace("ベ", "be").replace("ボ", "bo")
            .replace("パ", "pa").replace("ピ", "pi").replace("プ", "pu").replace("ペ", "pe").replace("ポ", "po")
            .replace("マ", "ma").replace("ミ", "mi").replace("ム", "mu").replace("メ", "me").replace("モ", "mo")
            .replace("ヤ", "ya").replace("ユ", "yu").replace("ヨ", "yo")
            .replace("ラ", "ra").replace("リ", "ri").replace("ル", "ru").replace("レ", "re").replace("ロ", "ro")
            .replace("ワ", "wa").replace("ヰ", "wi").replace("ヱ", "we").replace("ヲ", "wo")
            .replace("ン", "n");
            
        // 변환되지 않은 일본어가 있다면 원본 반환  
        if (romanized.matches(".*[\\u3040-\\u309F\\u30A0-\\u30FF].*")) {
            return japanese; // 완전히 변환되지 않으면 원본 유지
        }
        
        return romanized;
    }

}
