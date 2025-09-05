# 📚 QuestionCloud - 수능 문제 거래 플랫폼

> 이커머스 백엔드 개발 학습을 위해 진행하는 개인 프로젝트입니다.  
> 수능 문제 거래 플랫폼을 주제로 결제 시스템의 안정성과 확장 가능한 아키텍처 설계를 연습하고 있습니다.
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
- Prometheus + Grafana + Tempo(모니터링)

---

## 🏗️ 시스템 아키텍처

### 전체 인프라 구성

<img width="667" alt="QuestionCloud System Architecture" src="https://github.com/user-attachments/assets/fc48cbd0-6181-4ca8-95cc-e7d29e867b84" />

### 🎯 인프라 구성

- **AWS EC2**: Docker 기반 Spring Boot 애플리케이션
- **AWS RDS**: MariaDB 관리형 데이터베이스
- **AWS ElastiCache**: Redis
- **MongoDB Atlas**: 조회용 NoSQL 데이터베이스
- **AWS SNS/SQS**: 비동기 메시지 처리
- **모니터링**: Prometheus + Grafana

---

### 모듈러 모놀리식 아키텍처

```
📱 Runtime Container
    └── qc-api-container (Spring Boot Application)
        ├── 🏗️ Domain Modules Loading
        ├── 🌐 API Endpoints Aggregation  
        └── 🔄 Dependency Injection

🏗️ Domain Modules (Loaded by Container)
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

**🚀 Container 역할 (`qc-api-container`)**

- 모든 도메인 모듈을 Spring Context에 로딩
- 도메인별 API 엔드포인트를 단일 애플리케이션으로 통합
- 공통 설정 및 인프라 구성 관리
- 실제 배포되는 유일한 실행 가능한 애플리케이션

**📦 Domain Module 역할**

- **Core**: 비즈니스 로직 및 도메인 엔티티
- **API**: REST Controller 및 DTO 정의
- **Internal API**: 다른 도메인과의 통신 인터페이스 (선택적)
- Container에 의해 런타임에 로딩되어 API 엔드포인트 제공

**🔄 모듈 로딩 과정**

1. `qc-api-container` 시작
2. Classpath에서 모든 도메인 모듈 스캔
3. 각 도메인의 `@RestController`, `@Service`, `@Repository` 등을 Spring Context에 등록
4. 통합된 API 서버로 동작

## 🚀 구현 API

### 🔐 **인증 (Authentication)**

- **로그인** - 이메일과 비밀번호를 사용한 로그인
- **소셜 로그인** - KAKAO, GOOGLE, NAVER 소셜 계정 로그인
- **토큰 리프레시** - 리프레시 토큰을 통한 액세스 토큰 재발급

### 👤 **사용자 관리 (User)**

- **회원가입** - 이메일/소셜 계정을 통한 신규 사용자 등록
- **회원가입 인증** - 이메일 인증을 통한 계정 활성화
- **계정 찾기** - 전화번호를 통한 이메일 계정 찾기
- **비밀번호 찾기/변경** - 이메일을 통한 비밀번호 재설정
- **내 정보 조회/수정** - 사용자 프로필 관리

### 🛒 **스토어 - 장바구니 (Store Cart)**

- **장바구니 조회** - 담긴 문제 목록 확인
- **장바구니 담기** - 문제를 장바구니에 추가
- **장바구니 빼기** - 선택한 문제들을 장바구니에서 제거

### 📚 **스토어 - 상품 (Store Product)**

- **문제 목록 조회** - 필터링 및 페이징을 통한 문제 검색
- **문제 카테고리 조회** - 과목별 분류된 카테고리 목록
- **문제 상세 조회** - 개별 문제의 상세 정보

### ⭐ **스토어 - 리뷰 (Store Review)**

- **문제 리뷰 목록 조회** - 특정 문제에 대한 리뷰 목록 (페이징)
- **내가 작성한 리뷰 조회** - 특정 문제에 대한 내 리뷰 확인
- **문제 리뷰 등록** - 구매한 문제에 대한 리뷰 작성
- **문제 리뷰 수정/삭제** - 내가 작성한 리뷰 관리

### 📱 **라이브러리 (Library)**

- **나의 문제 목록 조회** - 보유한 문제 목록을 필터링 및 페이징하여 조회

### 👨‍🎨 **크리에이터 (Creator)**

- **크리에이터 정보 조회** - 크리에이터 프로필, 판매 통계, 구독자 수 등 상세 정보

### 🔔 **구독 (Subscribe)**

- **크리에이터 구독/구독 취소** - 관심 있는 크리에이터 팔로우 관리
- **나의 구독 목록 조회** - 구독 중인 크리에이터 목록 (페이징)
- **특정 크리에이터 구독 여부 확인** - 구독 상태 및 구독자 수 조회

### 💰 **결제 - 포인트 (Payment Point)**

- **보유 포인트 조회** - 현재 사용 가능한 포인트 확인
- **포인트 충전 주문 생성** - 결제창 호출 전 주문 생성
- **포인트 충전 결제 승인** - PG 연동을 통한 포인트 충전 처리
- **포인트 충전 완료 여부 조회** - 결제 상태 확인
- **포인트 충전 내역 조회** - 충전 히스토리 (페이징)

### 💳 **결제 - 문제 구매 (Payment Question)**

- **문제 구매** - 선택한 문제들을 포인트로 구매 (쿠폰 적용 가능)
- **문제 구매 내역 조회** - 구매 히스토리 및 주문 상세 정보 (페이징)

### 🎫 **결제 - 쿠폰 (Payment Coupon)**

- **사용 가능한 쿠폰 목록 조회** - 보유한 쿠폰 리스트
- **쿠폰 등록** - 쿠폰 코드를 통한 쿠폰 추가

### 📝 **게시판 (Post)**

- **문제 게시글 목록 조회** - 특정 문제에 대한 게시글 목록 (페이징)
- **문제 게시글 조회/등록/수정/삭제** - 게시글 CRUD 기능
- **문제 게시글 댓글 조회/작성/수정/삭제** - 댓글 CRUD 기능

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
