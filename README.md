# QuestionCloud - 수능 문제 거래 플랫폼

QuestionCloud는 수학, 물리, 지구과학, 화학, 생명과학 문제를 거래할 수 있는 플랫폼입니다.   
누구나 크리에이터가 될 수 있으며 본인이 만든 문제를 올려 판매할 수 있습니다.   
사용자는 포인트를 이용해 원하는 문제를 구매할 수 있으며, 포인트는 현금 결제를 통해 충전하는 방식으로 운영됩니다.   
이커머스의 비즈니스 모델을 구축하는 것을 목표로 하고 있으며 정교한 결제 시스템을 구축하는 것에 중점을 두었습니다.

# 프로젝트 아키텍처

<img width="667" alt="image" src="https://github.com/user-attachments/assets/122699cb-8c58-45c7-9db7-abd93542b933" />

# 프로젝트 Stack

Spring Boot, Maria DB, JPA, Querydsl, Redis, AWS SNS, AWS SQS, Mongo DB, Docker, AWS EC2, AWS RDS, AWS Elasticache

# 멀티 모듈

`qc-application` : 유저가 사용하는 API를 제공하는 Spring Boot Application입니다.

`qc-config` : Github Submodule을 이용하여 서버 설정에 필요한 환경변수를 관리합니다.

`qc-core` : 도메인 클래스, 도메인 로직, 엔티티 클래스, 레포지토리가 포함되어 있는 공통 모듈입니다.

`qc-external-pg-api`: 외부 PG 솔루션(Portone) API를 이용하여 결제를 처리하는 모듈입니다.

`qc-lock-manager` : Redis + Redisson을 이용하여 분산락을 처리하는 모듈입니다.

`qc-social-api` : KAKAO, NAVER, GOOGLE OAUTH API를 통해 소셜 서비스의 인증을 처리하는 모듈입니다

# 구현 API

**인증**

- 이메일 로그인
- 소셜 로그인 (KAKAO, NAVER, GOOGLE)
- 토큰 Refresh

**장바구니**

- 장바구니 조회
- 장바구니 상품 추가
- 장바구니 상품 삭제

**크리에이터**

- 크리에이터 정보 조회

**FEED**

- 내가 구매한 문제 목록 조회
- 내가 구독중인 크리에이터 조회

**문제 HUB**

- 문제 목록 조회 (필터링, 페이징)
- 문제 카테고리 조회
- 문제 상세 조회
- 문제 리뷰 조회 (페이징)
- 내가 작성한 문제 리뷰 조회
- 문제 리뷰 등록 / 수정 / 삭제

**포인트 충전**

- 포인트 충전 주문 생성
- 포인트 충전 (PG Webhook 용)
- 포인트 충전 결제 완료 여부 조회
- 포인트 충전 내역 조회

**문제 구매**

- 문제 구매
- 문제 구매 내역 조회

**문제 게시판**

- (문제 게시글/댓글) 등록 / 수정 / 삭제
- (문제 게시글/댓글) 조회 (페이징)

**구독**

- 크리에이터 구독/구독 취소
- 특정 크리에이터 구독 여부 조회

**사용자**

- 회원가입
- 계정 찾기 / 비밀번호 초기화
- 내 프로필 조회 / 수정
- 보유 포인트 조회

**쿠폰**

- 쿠폰 등록
- 사용 가능한 쿠폰 조회

**크리에이터 WorkSpace**

- 크리에이터 등록 신청
- 크리에이터 프로필 조회 / 수정
- 자작 문제 등록 / 수정 / 삭제
