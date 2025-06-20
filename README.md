# 📚 QuestionCloud - 수능 문제 거래 플랫폼

> 수학, 물리, 지구과학, 화학, 생명과학 문제를 거래할 수 있는 **개인 학습용 프로젝트**  
> 모듈러 모놀리식 아키텍처와 안정적인 결제 시스템 구현에 중점을 둔 **백엔드 개발 연습 프로젝트**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-6DB33F?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8+-7F52FF?style=flat-square&logo=kotlin)](https://kotlinlang.org/)
[![MariaDB](https://img.shields.io/badge/MariaDB-10.6+-003545?style=flat-square&logo=mariadb)](https://mariadb.org/)

---

## 📋 프로젝트 소개

이커머스 백엔드 개발 학습을 위해 진행하는 개인 프로젝트입니다.  
수능 문제 거래 플랫폼을 주제로 **결제 시스템의 안정성**과 **확장 가능한 아키텍처 설계**를 연습하고 있습니다.

### 🎯 **학습 목표**

- 복잡한 도메인을 체계적으로 모듈화하는 경험
- 안전한 결제 시스템 구현 (메시지 유실 방지, 데이터 정합성)
- 모듈러 모놀리식 아키텍처 설계 및 적용
- 실제 운영을 고려한 모니터링 및 로깅 구현

---

## 🏗️ 아키텍처 구조

### 모듈러 모놀리식 아키텍처 적용

```
📱 Application Layer
    ├── qc-api-container (메인 API 서버)
    └── qc-admin-application (관리자 앱, 계획중)

🏗️ Domain Layer  
    ├── 👤 qc-user (Core + API + Internal API)
    ├── ❓ qc-question (Core + API + Internal API)  
    ├── 👨‍🎨 qc-creator (Core + API + Internal API)
    ├── 🏆 qc-point (Core + API + Internal API)
    ├── 📝 qc-post (Core + API + Internal API)
    ├── 🛒 qc-cart (Core + API)
    ├── ⭐ qc-review (Core + API)
    ├── 💳 qc-pay (Core + API)
    ├── 🔔 qc-subscribe (Core + API)
    └── 🎫 qc-coupon (Core + API)

🔌 Interface Layer
    └── qc-internal-api-interface/* (도메인 간 통신)

🛠️ Infrastructure Layer  
    ├── qc-rdb (데이터베이스)
    ├── qc-event (이벤트 처리)
    ├── qc-external-pg-api (결제 게이트웨이)
    ├── qc-lock-manager (분산 락)
    ├── qc-logging (API 로깅)
    └── qc-social (소셜 로그인)

🔧 Shared Layer
    └── qc-common (공통 유틸리티)
```

#### 모듈 구성 (총 45개 모듈)

```
📱 Application (2개)     - API Container, Admin API Container(계획)
🏗️ Domain (30개)         - 10개 비즈니스 도메인  
🔌 Interface (6개)       - 도메인 간 통신 인터페이스
🛠️ Infrastructure (6개)  - 기술적 기반 모듈
🔧 Shared (1개)          - 공통 유틸리티
```

#### 주요 도메인 모듈

| 도메인           | 구성                        | 설명       |
|---------------|---------------------------|----------|
| 👤 User       | Core + API + Internal API | 사용자 관리   |
| ❓ Question    | Core + API + Internal API | 문제 관리    |
| 👨‍🎨 Creator | Core + API + Internal API | 크리에이터 관리 |
| 🏆 Point      | Core + API + Internal API | 포인트 시스템  |
| 🛒 Cart       | Core + API                | 장바구니     |
| 💳 Pay        | Core + API                | 결제 처리    |
| ⭐ Review      | Core + API                | 리뷰 시스템   |

---

## 🛠️ 사용 기술

**Backend Framework**

- Spring Boot 3.x, Kotlin
- Spring Data JPA, Querydsl

**Database**

- MariaDB (메인 RDBMS)
- MongoDB (조회용 데이터)
- Redis (캐시 & 분산 락)

**Message Queue**

- AWS SNS + SQS (비동기 이벤트 처리)

**Infrastructure**

- Docker, AWS EC2, AWS RDS
- Prometheus + Grafana (모니터링)

---

## 📁 모듈 구조

### Application Layer

- **`qc-api-container`** - 메인 API 서버

### Domain Layer

- **`qc-domain/*`** - 도메인별 모듈
    - `*-core`: 비즈니스 로직
    - `*-api`: REST Controller
    - `*-internal-api`: 도메인 간 통신 (필요시)

### Infrastructure Layer

- **`qc-rdb`** - 데이터베이스 설정
- **`qc-event`** - 이벤트 처리 (AWS SNS/SQS)
- **`qc-external-pg-api`** - 결제 게이트웨이 (TossPayments)
- **`qc-lock-manager`** - 분산 락 (Redis)
- **`qc-logging`** - API 로깅
- **`qc-social`** - 소셜 로그인

---

## 🚀 구현 API

### 🔐 **인증**

- 이메일 로그인
- 소셜 로그인 (KAKAO, NAVER, GOOGLE)
- 토큰 Refresh

### 🛒 **장바구니**

- 장바구니 조회
- 장바구니 상품 추가
- 장바구니 상품 삭제

### 👨‍🎨 **크리에이터**

- 크리에이터 정보 조회

### 📱 **FEED**

- 내가 구매한 문제 목록 조회
- 내가 구독중인 크리에이터 조회

### 📚 **문제 HUB**

- 문제 목록 조회 (필터링, 페이징)
- 문제 카테고리 조회
- 문제 상세 조회
- 문제 리뷰 조회 (페이징)
- 내가 작성한 문제 리뷰 조회
- 문제 리뷰 등록 / 수정 / 삭제

### 🏆 **포인트 충전**

- 포인트 충전 주문 생성
- 포인트 충전 (PG Webhook 용)
- 포인트 충전 결제 완료 여부 조회
- 포인트 충전 내역 조회

### 💰 **문제 구매**

- 문제 구매
- 문제 구매 내역 조회

### 📝 **문제 게시판**

- (문제 게시글/댓글) 등록 / 수정 / 삭제
- (문제 게시글/댓글) 조회 (페이징)

### 🔔 **구독**

- 크리에이터 구독/구독 취소
- 특정 크리에이터 구독 여부 조회

### 👤 **사용자**

- 회원가입
- 계정 찾기 / 비밀번호 초기화
- 내 프로필 조회 / 수정
- 보유 포인트 조회

### 🎫 **쿠폰**

- 쿠폰 등록
- 사용 가능한 쿠폰 조회

### 🏢 **크리에이터 WorkSpace**

- 크리에이터 등록 신청
- 크리에이터 프로필 조회 / 수정
- 자작 문제 등록 / 수정 / 삭제

---

## 🎯 개선 및 문제 해결 사례

### 🚀 **성능 최적화**

#### **MongoDB 조회 성능 개선** - [관련 포스팅](https://e4g3r.tistory.com/8)

- Query에서 사용되는 필드들을 복합 인덱스로 지정하여 인덱스 정렬을 통한 별도의 정렬 과정 제거
- Spring Data Mongo Custom Converter를 통한 POJO Mapping 성능 개선
- **TPS 1,937 → 3,900 (101% 향상)**

#### **리뷰 평점 정렬 성능 개선** - [관련 포스팅](https://e4g3r.tistory.com/3)

- 리뷰 통계 테이블을 설계하여 리뷰 추가/수정/삭제 시 통계 테이블의 평점을 갱신하는 방식으로 변경
- SUM, COUNT 연산 없이 통계 테이블을 이용하여 평점 순으로 정렬
- **리뷰 데이터 1000만 건 기준 SQL 처리 시간 3s → 0.149s (95% 향상)**

#### **AWS SNS 토픽 발행 처리 속도 개선** - [관련 포스팅](https://e4g3r.tistory.com/26)

- Coroutine을 통해 이벤트 재발행을 처리했으나 SNS 토픽 발행 HTTP 통신으로 인한 쓰레드 Blocking 발생
- Non-Blocking을 지원하는 AWS SNS 라이브러리로 변경하고 Max Connections를 증가시켜 동시 처리량 향상
- **10,000건 SNS 토픽 기준 처리 시간 4.7s → 2.2s (53% 향상)**

### 🛡️ **안정성 보장**

#### **메시지 발행 및 파일 기반 로깅을 통한 결제 실패 처리 보장** - [관련 포스팅](https://e4g3r.tistory.com/12)

- 메시지 브로커는 메시지가 정상적으로 소비될 때까지 메시지 큐에서 유실되지 않는다는 점을 이용
- 결제 실패 메시지를 발행하여 결제 취소 처리 및 롤백을 보장받도록 구현
- 메시지 발행에 실패하는 경우에는 파일 기반 로그를 통해 결제 실패 사실이 유실되지 않도록 보장

#### **Transactional Outbox Pattern을 이용한 메시지 발행 보장** - [관련 포스팅](https://e4g3r.tistory.com/13)

- 비즈니스 로직과 이벤트 발행 로그 저장 로직을 하나의 트랜잭션으로 처리
- 이벤트 발행에 실패하더라도 주기적으로 이벤트 발행 로그를 확인하여 발행되지 않은 이벤트를 재발행
- 메시지 발행을 보장하여 데이터 정합성을 보장

#### **멱등성 테이블을 통한 메시지 중복 처리 방지** - [관련 포스팅](https://e4g3r.tistory.com/14)

- 메시지 Listener는 비즈니스 로직을 수행한 후 MessageID와 Listener 이름 기반의 멱등성 키를 멱등성 테이블에 저장
- 비즈니스 로직과 멱등성 키를 저장하는 로직은 하나의 트랜잭션으로 처리
- 멱등성 키 조회 및 저장 로직은 AOP를 이용하여 필요한 Listener에서 재사용할 수 있도록 구현

### 🏗️ **아키텍처 개선**

#### **Modular Monolithic Architecture + 멀티 모듈 설계** - [관련 포스팅](https://e4g3r.tistory.com/41)

- 단일 모듈에서 도메인별 45개 모듈로 체계적 분리
- 도메인별 Controller + Core + Internal API 구조로 설계하여 각 도메인의 독립성 확보
- 모듈 간 통신은 Interface 계층을 통해 이루어지도록 설계하여 도메인 간 결합도 제거

#### **조회용 결제 내역 모델을 통한 조회 로직 개선** - [관련 포스팅](https://e4g3r.tistory.com/7)

- RDBMS에서 관리되던 결제 내역 데이터를 반정규화 과정을 통해 조회용 모델로 설계하여 MongoDB에 저장
- 간단한 쿼리로 결제 내역 데이터를 조회할 수 있게 되어 Repository 조회 코드의 가독성 개선
- 상품 정보가 수정되거나 삭제되더라도 구매 시점의 데이터로 조회 가능

### 📊 **모니터링 구축**

#### **모니터링을 위한 APM 도구 도입** - [관련 포스팅](https://e4g3r.tistory.com/36)

- Spring Actuator + Prometheus + Grafana를 활용한 Metrics 기반 서버 상태 모니터링 구축
- Spring Micrometer + Brave Tracer + Grafana Tempo를 활용한 Trace 데이터 수집 및 모니터링 구축

---

**📚 개인 학습용 프로젝트로, 지속적으로 개선하며 백엔드 개발 역량을 키워가고 있습니다.**