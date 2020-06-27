# payServiceDemo Project

## 사용법

```
1. jar 파일 다운로드 : https://drive.google.com/file/d/1Ww9gUNQKgxHz1I1UWBHt0rV4lcAdv3c9/view?usp=sharing
2. jar 파일 실행 : java -jar pokeball-0.0.1-SNAPSHOT.jar
```

## 핵심 문제 해결 전략
```
1. Http Header, Body 를 간편하게 처리하기 위해 Spring Annotation 활용
2. Stateless 한 WebApplication 작성을 위해 데이터 처리 시 DB 활용, 데모 버전 개발 간편화를 위해 Spring Data JPA와 H2 Embedded DB 사용
3. 각 Controller, Service, Repository 별 Test 코드 작성
4. Token 생성 시 공개된 소스코드 참조
  - https://codereview.stackexchange.com/questions/159421/generate-16-digit-unique-code-like-product-serial
  - https://github.com/js42721/fastrandom/blob/master/src/main/java/fastrandom/WELL512.java
```

## 개선 필요 사항
```
1. Token 값 유효성 검증 로직 추가 필요
2. 다량의 트래픽 발생 시 부하를 줄일 수 있도록 Cache 활용 고려 필요
```

## API 정의 문서

### 뿌리기

```
[REQUEST] POST /api/send 

X-USER-ID: Test001
X-ROOM-ID: Room001
{
  "send_tot_amt": 10001,
  "send_tot_cnt": 3
}

[RESPONSE]

{
 "token":"G4N"
}

```

### 받기

```
[REQUEST] PUT /api/recv?token=G4N

X-USER-ID: Test002
X-ROOM-ID: Room001
{

}

[RESPONSE]

{
 "recv_amt":100.00
}

```

### 조회

```
[REQUEST] GET /api/send?token=G4N

X-USER-ID: Test001
X-ROOM-ID: Room001

[RESPONSE]

{
 "created_at":"2020-06-27T14:24:01.408",
 "recv_amt":200.00,
 "recv_user_info_vo":
  [
    {
      "recv_amt": 100.00,
      "recv_user_id": "Test004"
    },
    {
      "recv_amt": 100.00,
      "recv_user_id": "Test005"
    }
  ],
 "send_tot_amt":10001.00
}

```
