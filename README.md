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

## 초기 데이터

postgres DB 기동 시 입력되는 초기 데이터

### 사용자

```
 id |                   name                   
----+------------------------------------------
  1 | ㅋㅋㅇ
  2 | 콩심은데콩나고팥심은데팥난다꼭명심하세요
  3 | Guile
  4 | 김갑환
  5 | Chun Li
  6 | Kyoshiro
  7 | Haomaru
  8 | Charlotte
  9 | Bruce
(9 rows)
```

### 대화방

- `owner_id`: 방장

```
                  id                  | name | owner_id 
--------------------------------------+------+----------
 4cf55070-10ae-4097-afcf-d61a25cfd233 | 방1  |        1
 74b45ce5-0939-4eff-bcd3-8a6277066252 | 방2  |        2
 b7dd1bf7-bf20-48f8-98ff-558f04faa35f | 방3  |        2
 c70f1bbc-787d-4706-aac1-54cc609997ca | 방4  |        2
 cf12c71e-e81e-4c41-9750-187f670b0847 | 방5  |        3
 f5addd51-2d45-47a2-8d15-d95ece9b21b8 | 방6  |        5
 396d7ac2-aa65-4f56-8ec1-ccc8b88b332c | 방7  |        5
 23662be4-2230-42c9-a93b-a58049fbd414 | 방8  |        7
 c2cef0fb-7108-4f5e-92e3-9b3011f77fbf | 방9  |        9
(9 rows)
```

### 대화방 참여자

```
 id |             chat_room_id             | chatter_id 
----+--------------------------------------+------------
  1 | 4cf55070-10ae-4097-afcf-d61a25cfd233 |          1
  2 | 4cf55070-10ae-4097-afcf-d61a25cfd233 |          2
  3 | 4cf55070-10ae-4097-afcf-d61a25cfd233 |          3
  4 | 4cf55070-10ae-4097-afcf-d61a25cfd233 |          4
  5 | 4cf55070-10ae-4097-afcf-d61a25cfd233 |          5
  6 | 74b45ce5-0939-4eff-bcd3-8a6277066252 |          2
  7 | b7dd1bf7-bf20-48f8-98ff-558f04faa35f |          2
  8 | b7dd1bf7-bf20-48f8-98ff-558f04faa35f |          4
  9 | b7dd1bf7-bf20-48f8-98ff-558f04faa35f |          6
 10 | c70f1bbc-787d-4706-aac1-54cc609997ca |          2
 11 | cf12c71e-e81e-4c41-9750-187f670b0847 |          3
 12 | f5addd51-2d45-47a2-8d15-d95ece9b21b8 |          5
 13 | 396d7ac2-aa65-4f56-8ec1-ccc8b88b332c |          5
 14 | 23662be4-2230-42c9-a93b-a58049fbd414 |          7
 15 | c2cef0fb-7108-4f5e-92e3-9b3011f77fbf |          9
(15 rows)
```

### 머니 뿌리기 (distribution)

- `distributor_id`: 뿌린 사용자
- `targets`: 받을 사람 수
- `amount`: 뿌린 총 금액

```
 id | amount | targets | token |             chat_room_id             | distributor_id 
----+--------+---------+-------+--------------------------------------+----------------
  1 |    100 |       2 | a11   | 4cf55070-10ae-4097-afcf-d61a25cfd233 |              1    // 20분 전 생성
  2 |    400 |       4 | a21   | 4cf55070-10ae-4097-afcf-d61a25cfd233 |              2    // 10일 전 생성
  3 |    300 |       1 | c41   | b7dd1bf7-bf20-48f8-98ff-558f04faa35f |              4
(3 rows)
```

### 머니 받기 (receipt)

- 아직 아무도 받지 않은 상태
- `status.OPEN`: 안 받은 상태
- `status.CLOSED`: 받은 상태
- `amount`: 받은 개별 금액

```
 id | amount | receiver_id | status | distribution_id 
----+--------+-------------+--------+-----------------
  1 |     50 |             | OPEN   |               1
  2 |     50 |             | OPEN   |               1
  3 |    100 |             | OPEN   |               2
  4 |    100 |             | OPEN   |               2
  5 |    100 |             | OPEN   |               2
  6 |    100 |             | OPEN   |               2
  7 |    300 |             | OPEN   |               3
(7 rows)
```

# 테스트

## 뿌리기 

### 뿌리기 생성

- `distribution` 테이블에 1건, `receipt` 테이블에 `targets`건 데이터 추가됨
- 분배 금액은 나눠떨어지면 균등, 아니면 첫 수령자가 나머지를 포함한 금액 수령

```
curl --request POST \
  --url http://localhost:9000/distributions \
  --header 'content-type: application/json' \
  --header 'x-room-id: 4cf55070-10ae-4097-afcf-d61a25cfd233' \
  --header 'x-user-id: 3' \
  --data '{
	"amount": 200,
	"targets": 2
}'
```

## 받기

### 받기 성공

- 뿌리기 생성에서 받은 token 값 입력

```
curl --request POST \
  --url http://localhost:9000/receipts \
  --header 'content-type: application/json' \
  --header 'x-room-id: 4cf55070-10ae-4097-afcf-d61a25cfd233' \
  --header 'x-user-id: 5' \
  --data '{
	"token": "4ML"
}'
```

### 중복 수령 불가

```
curl --request POST \
  --url http://localhost:9000/receipts \
  --header 'content-type: application/json' \
  --header 'x-room-id: 4cf55070-10ae-4097-afcf-d61a25cfd233' \
  --header 'x-user-id: 5' \
  --data '{
	"token": "4ML"
}'
```

```
{
  "statusCode": 400,
  "errorCode": "COMMON_005",
  "message": "동일한 뿌리기에서 중복 수령은 허용되지 않습니다.",
  "timestamp": "2020-06-27T17:47:26.048613",
  "requestUrl": "http://5b58297918fe:8080/receipts"
}
```

### 뿌린 사람은 수령 불가

```
curl --request POST \
  --url http://localhost:9000/receipts \
  --header 'content-type: application/json' \
  --header 'x-room-id: 4cf55070-10ae-4097-afcf-d61a25cfd233' \
  --header 'x-user-id: 3' \
  --data '{
	"token": "4ML"
}'
```

```
{
  "statusCode": 400,
  "errorCode": "COMMON_005",
  "message": "자기가 뿌린 머니는 수령할 수 없습니다.",
  "timestamp": "2020-06-27T17:45:56.590137",
  "requestUrl": "http://d98d6db543db:8080/receipts"
}
```

### 대화방에 없는 사람은 수령 불가

```
curl --request POST \
  --url http://localhost:9000/receipts \
  --header 'content-type: application/json' \
  --header 'x-room-id: 4cf55070-10ae-4097-afcf-d61a25cfd233' \
  --header 'x-user-id: 9' \
  --data '{
	"token": "4ML"
}'
```

```
{
  "statusCode": 400,
  "errorCode": "COMMON_005",
  "message": "참여하지 않은 대화방에 있는 뿌리기는 수령할 수 없습니다.",
  "timestamp": "2020-06-27T17:47:59.019938",
  "requestUrl": "http://a681ce49f49f:8080/receipts"
}
```

### 10분 이전에 뿌려진 머니는 받기 불가

```
curl --request POST \
  --url http://localhost:9000/receipts \
  --header 'content-type: application/json' \
  --header 'x-room-id: 4cf55070-10ae-4097-afcf-d61a25cfd233' \
  --header 'x-user-id: 3' \
  --data '{
	"token": "a11"
}'
```

```
{
  "statusCode": 400,
  "errorCode": "COMMON_005",
  "message": "10분 이상 경과된 뿌리기는 수령할 수 없습니다.",
  "timestamp": "2020-06-27T17:49:38.128901",
  "requestUrl": "http://d98d6db543db:8080/receipts"
}
```

## 조회

### 뿌리기 조회 성공

```
{
  "id": 4,
  "token": "4ML",
  "distributorId": 3,
  "chatRoomId": "4cf55070-10ae-4097-afcf-d61a25cfd233",
  "distributedAt": "2020-06-27T17:34:33",
  "amount": 200,
  "targets": 2,
  "receivedAmount": 100,
  "receipts": [
    {
      "id": 8,
      "receiverId": 5,
      "amount": 100
    }
  ]
}
```

```
{
  "id": 4,
  "token": "4ML",
  "distributorId": 3,
  "chatRoomId": "4cf55070-10ae-4097-afcf-d61a25cfd233",
  "distributedAt": "2020-06-27T17:34:33",
  "amount": 200,
  "targets": 2,
  "receivedAmount": 100,
  "receipts": [
    {
      "id": 8,
      "receiverId": 5,
      "amount": 100
    }
  ]
}
```

### 뿌리지 않은 사람은 뿌리기 조회 불가

```
curl --request GET \
  --url 'http://localhost:9000/distributions?token=4ML' \
  --header 'content-type: application/json' \
  --header 'x-room-id: 4cf55070-10ae-4097-afcf-d61a25cfd233' \
  --header 'x-user-id: 6'
```

```
{
  "statusCode": 400,
  "errorCode": "COMMON_004",
  "message": "자기가 뿌린 뿌리기 정보만 조회할 수 있습니다.",
  "timestamp": "2020-06-27T17:55:19.606245",
  "requestUrl": "http://a681ce49f49f:8080/distributions"
}
```

### 존재하지 않는 토큰으로 뿌리기 조회 불가

```
curl --request GET \
  --url 'http://localhost:9000/distributions?token=zzz' \
  --header 'content-type: application/json' \
  --header 'x-room-id: 4cf55070-10ae-4097-afcf-d61a25cfd233' \
  --header 'x-user-id: 6'
```

```
{
  "statusCode": 400,
  "errorCode": "COMMON_004",
  "message": "토큰 [zzz]에 해당하는 뿌리기가 존재하지 않습니다.",
  "timestamp": "2020-06-27T17:56:47.47447",
  "requestUrl": "http://d98d6db543db:8080/distributions"
}
```

### 7일 지난 뿌리기는 조회 불가

```
curl --request GET \
  --url 'http://localhost:9000/distributions?token=a21' \
  --header 'content-type: application/json' \
  --header 'x-room-id: 4cf55070-10ae-4097-afcf-d61a25cfd233' \
  --header 'x-user-id: 2'
```

```
{
  "statusCode": 400,
  "errorCode": "COMMON_004",
  "message": "뿌린 지 7일 이내 뿌리기 정보만 조회할 수 있습니다.",
  "timestamp": "2020-06-27T18:00:13.728532",
  "requestUrl": "http://5b58297918fe:8080/distributions"
}
```
