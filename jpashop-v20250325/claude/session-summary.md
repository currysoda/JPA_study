# 세션 요약 (2026-05-27)

## 프로젝트 개요

- **목적**: JPA/Hibernate 학습용 프로젝트 (실제 배포용 아님)
- **스택**: Spring Boot 3.4.4, Java 17, H2 (TCP 모드), Thymeleaf, Lombok
- **H2 URL**: `jdbc:h2:tcp://localhost/~/jpashop`
- **JPA 설정**: `ddl-auto: create` (재시작 시 테이블 재생성)

## 주요 도메인 구조

| 엔티티 | 특이사항 |
|--------|---------|
| `Member` | Address @Embeddable 임베딩, 1:N Orders, `@NoArgsConstructor(PROTECTED)` + `@Builder` |
| `Order` | UUID 기반 orderNumber, Cascade → Delivery/OrderItems |
| `OrderItem` | Order ↔ Item 연결 |
| `Item` (abstract) | Single Table 상속 → Book, Album, Movie |
| `Delivery` | 1:1 Order |
| `Category` | 자기 참조 계층, CategoryItem으로 N:M Item 연결 |
| `BaseEntity` | createdAt/updatedAt/deletedAt/isDeleted (소프트 딜리트) |

## 구현 완료 / 미완성 현황

### 완료
- `MemberController` 인터페이스 + `MemberControllerV1` 구현체 (등록/목록 정상 동작)
- `MemberService` 인터페이스 + `MemberServiceImpl` + 테스트 6개
- `ItemService` (updateItem 더티 체킹 포함)
- `ItemController` (전체)
- `OrderRepository` (JPQL + Criteria API)

### 미완성 (주석 처리 상태)
- `OrderServiceImpl` — 대부분 스켈레톤
- `OrderController` — 대부분 메서드 주석 처리
- `OrderServiceImplTest` — 스켈레톤만 존재

## 컨트롤러 구조 패턴

인터페이스 + V1/V2 구현체 패턴 적용 중:

```
member/controller/
├── MemberController.java       ← 인터페이스 (메서드 계약 정의)
└── MemberControllerV1.java     ← 현재 구현체 (@Controller, URL 매핑 포함)
```

## 주요 학습 포인트

### `@NoArgsConstructor(access = AccessLevel.PROTECTED)` + `@Builder`
- JPA용 기본 생성자는 필요하지만 외부 직접 생성(`new Member()`) 방지
- 외부에서는 반드시 `Member.builder()...build()` 로 생성

### 트랜잭션 전파
- `@Transactional` 은 AOP 프록시 기반 → 같은 클래스 내 private 메서드 호출은 프록시 미경유
- `join()` 안에서 호출되는 `validateDuplicateMember()` 는 "합류"가 아닌 동일 트랜잭션(ThreadLocal 바인딩) 위에서 그냥 실행됨
- 클래스 레벨 `@Transactional(readOnly = true)` + 쓰기 메서드만 `@Transactional` 오버라이드하는 패턴

### 레이어별 메서드 네이밍
- Repository: `findById`, `findByName` 등 `find` 계열
- Service: `getMember`, `getMembers` 등 `get` 계열이 서비스 레이어답게 어울림

### P6Spy
- JDBC 인터셉터 — SQL 로그의 `?` 대신 실제 바인딩 값 출력
- `build.gradle` 의존성만으로 자동 활성화 (`spy.properties` 불필요)
- 학습/디버깅용, 운영 환경 부적합

## Claude Code 세션 공유
| 조합 | 대화 내역 공유 |
|------|--------------|
| Claude Code CLI ↔ Claude Code 데스크탑 앱 (같은 프로젝트) | ✅ 공유됨 |
| Claude Code ↔ Claude.ai 웹/앱 채팅 | ❌ 공유 안 됨 |
