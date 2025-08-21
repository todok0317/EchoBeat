# EchoBeat - MSA Architecture Design

## 🎵 프로젝트 개요
**EchoBeat**: K-Pop + J-Pop 글로벌 음악 차트 서비스
- 기존 JPop Ranker 코드를 MSA로 확장
- 실시간 순위 + 사용자 인터랙션 + 개인화 추천

## 🏗️ 마이크로서비스 구조

### 1. **API Gateway** (Port: 8080)
- Spring Cloud Gateway
- 인증/인가 통합 처리
- Rate Limiting
- Load Balancing

### 2. **User Service** (Port: 8081)
- 회원가입/로그인 (JWT + OAuth2)
- 프로필 관리
- 팔로우/팔로워 기능
- **Database**: users, user_profiles, user_follows

### 3. **Ranking Service** (Port: 8082)
- **기존 JPop Ranker 코드 마이그레이션**
- K-Pop + J-Pop 차트 집계
- 실시간 순위 API
- Redis 캐싱으로 성능 최적화
- **Database**: songs, charts, rankings

### 4. **Playlist Service** (Port: 8083)
- 플레이리스트 CRUD
- 곡 담기/빼기
- 플레이리스트 공유
- **Kafka Event**: playlist_updated → Ranking Service
- **Database**: playlists, playlist_songs

### 5. **Recommendation Service** (Port: 8084)
- 개인화 추천 알고리즘
- Collaborative Filtering
- 인기 곡 추천
- **Kafka Consumer**: 사용자 행동 데이터 수집
- **Database**: user_preferences, recommendations

### 6. **Crawler Service** (Port: 8085)
- **기존 크롤링 로직 확장**
- Billboard Japan + K-Pop 차트 크롤링
- Scheduled Batch Job
- **Kafka Producer**: new_chart_data → Ranking Service

### 7. **Notification Service** (Port: 8086)
- 실시간 알림
- WebSocket 기반
- 새 차트 업데이트 알림
- 팔로우한 유저 플레이리스트 업데이트

## 🛠️ 기술 스택

### Backend
- **Framework**: Spring Boot 3.x
- **Database**: MySQL 8.0 (각 서비스별 DB 분리)
- **Cache**: Redis 7.x
- **Message Queue**: Apache Kafka
- **Service Discovery**: Eureka Server
- **API Gateway**: Spring Cloud Gateway
- **Config Management**: Spring Cloud Config

### DevOps
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **CI/CD**: GitHub Actions
- **Monitoring**: Prometheus + Grafana
- **Logging**: ELK Stack

### Frontend
- **Framework**: React 18 (기존 코드 확장)
- **State Management**: Redux Toolkit
- **UI Library**: Tailwind CSS (현재 스타일 유지)

## 📊 데이터 플로우

### 차트 업데이트 플로우
```
Crawler Service → Kafka (chart_updated) → Ranking Service → Redis 캐시 업데이트
```

### 사용자 인터랙션 플로우
```
Frontend → API Gateway → Playlist Service → Kafka (user_action) → Recommendation Service
```

### 실시간 알림 플로우
```
Ranking Service → Kafka (ranking_changed) → Notification Service