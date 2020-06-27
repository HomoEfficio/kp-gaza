# KP-Gaza

## 아키텍처 개요

- 다중 App 인스턴스를 사용할 수 있도록 마이크로서비스 패턴 중 일부 적용
    - Edge Server: Spring Cloud Gateway
    - Service Discovery: Spring Cloud Consul
- 1차는 Spring MVC로 구현하고, ~~자원 사용 효율성을 높이기 위해 2차로 Spring WebFlux + R2DBC로 전환~~
    - R2DBC 로 전환은 사실 상 불가로 폐기
- docker-compose 로 실행 환경 구성
- 동시 쓰기 발생 가능성이 높은 머니 받기 테이블(receipt)에 Optimistic Lock 적용


## 개발 방식

- 컨트롤러 계층을 중심으로 하는 TDD 적용
    - Commit Log 참고
    - 비즈니스 로직 검증을 위해 필요힌 경우 도메인 모델 클래스에 대한 TDD 병행
- DDD 개념 적용
    - 비즈니스 로직은 도메인 모델에, 애플리케이션 로직은 서비스 및 컨트롤러에 배치


## 개발 프레임워크 및 라이브러리

- Java 14
- Gradle 6.4.1
- Spring Boot 2.3.1
- Hibernate 5.4.17
- Postgres 12.3
- Docker Desktop 2.3.0.3
- Docker Compose yml 2.4


## 모듈 구조

- 단일 모듈로 구성
    - `_common`: 공통 모듈
    - `_config`: 설정 모듈
    - `controller`: 컨트롤러
    - `domain`: 도메인 모델 및 리포지토리
    - `dto`: DTO
    - `service`: 서비스

## 빌드

- APP 빌드:
    - money-distribution 폴더에서 `./gradlew clean bootJar`
    - edge-server 폴더에서 `./gradlew clean build bootJar`
        - h2 DB로 테스트
- Docker 이미지 빌드:
    - 프로젝트 루트 폴더에서 `docker-compose build`

## 실행

- App 단일 인스턴스 실행: `docker-compose up -d`
- App 다중 인스턴스 실행: `docker-compose up -d --scale money-distribution=3` - 3개 실행
