# Pay Practice

## 프로젝트 설명

페이 서비스를 만들어보며, **계좌 관리 및 결제 트랜잭션을 다루는 작은 금융 서비스**를 설계하고 구현하였습니다.
본 프로젝트에서는 **다중 계좌 관리 및 외부 결제 연동 시 발생하는 트랜잭션 처리 문제를 해결하는 데 집중**했습니다.

[개발 중 고민한 것 기록](https://coffee-sidewalk-a8c.notion.site/1a459a6ae03280baaa50c7b75840e91f?pvs=74)

### 개발 환경
Java 21, Spring boot 3.4, JPA, MySQL, Redis


## Feature
1. 계좌 생성 및 충전
- [x]  신규 사용자는 가입을 진행하면서 하나의 메인 계좌를 생성하고, 메인 계좌 충전에 필요한 타행 계좌를 등록한다.
- [x]  사용자는 적금 계좌를 생성할 수 있다.
- [x]  메인 계좌는 이체 시 잔액이 부족하면 가입 시 등록한 타행 계좌에서 돈을 가져온다.
- [x]  사용자는 타행 계좌에서 최대 3,000,000원을 인출할 수 있다.
- [x]  적금 계좌에 돈을 납입할 때는, 메인 계좌에서 인출해 납입한다.
    - 이 때도 메인 계좌에 돈이 부족하면 타행 계좌에서 인출한다.

2. 외부 결제로 인한 출금
- [x]  외부 결제 시 사용자의 메인 계좌의 출금이 이루어 지며, 결제 내역을 남겨야한다.
- [x]  출금 시, 내 메인 계좌에 잔액이 부족하면 나의 타행 계좌에서 돈을 가져온다.
    - 이 때도 타행 계좌 일일 인출 한도는 동일하게 적용
- [x]  외부 결제 승인 API는 멱등성을 지원해야한다.
    - 중복 요청 발생 시 HTTP Status 409 conflict 전달한다.

## DB Schema

![image](https://github.com/user-attachments/assets/e57a9d17-99c9-40dc-8f6e-f6b4bf15bab3)

- user : 사용자 정보
- account : 계좌 정보
- external_account : 외부 연결 계좌 정보
- transaction : 거래 내역 (입금, 출금, 외부 결제)
- payment : 외부 결제 정보

## API 명세

### 1. 사용자 가입

- 사용자 가입
- 메인 계좌 자동 생성
- 타행 계좌 등록

#### Request
```http
POST /users
```
request body
```json
{
  "name": "홍길동",
  "email": "hello@gmail",
  "externalBank" : "HANA BANK",
  "externalAccountNumber" : "1234567890"
}
```

### 2. 적금 계좌 생성

#### Request
```http
POST /accounts/savings
```

request body
```json
{
  "userId": 1
}
```

### 3. 적금 계좌로 입금

#### Request
```http
PATCH /accounts/savings/deposit
```

request body
```json
{
  "userId": 1,
  "amount": 10000
}
```

### 4. 외부 결제 승인

#### Request
```http
POST /payments
Idempotency-Key: test-idempotency-key
```

request body
```json
{
  "userId": 1,
  "partnerId": 100,
  "partnerPayKey": "partner-key-123",
  "productItems": [
    {
      "count": 1,
      "name": "Product A"
    },
    {
      "count": 2,
      "name": "Product B"
    }
  ],
  "totalPayAmount": 23000
}

```

#### Response

response body
```json
{
  "code": "Success",
  "message": "성공",
  "detail": {
    "paymentId": 15,
    "partnerPayKey": "partner-key-123",
    "totalPayAmount": 23000,
    "paymentDateTime": "2025-02-26 00:55:56"
  }
}
```
### 에러 응답

- HTTP Status: 409 Conflict 
  - 멱등성 보장 API의 Idempotency key 중복 발생


- HTTP Status: 429 Too Many Requests
   ```json
   {
     "code": "NotEnoughAccountBalance",
     "message": "충전 계좌 잔고 부족"
   }
   ```

- HTTP Status: 500 Internal Server Error
   ```json
   {
     "code": "BankMaintenance",
     "message": "충전 계좌 점검"
   }
   ```
   
   ```json
   {
      "code": "Fail",
      "message": "서비스 중 에러가 발생했습니다."
   }
   ```