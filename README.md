## API 명세
```
계좌 목록 조회
GET /accounts

response
[{
  "accountId": 1,
  "money": 1000
}]

계좌 이체
PATCH /accounts/{accountId}

request
{
  "fromUserId": 1, //송금자의 회원 ID
  "fromAccountId": 3, //송금자의 계좌 ID
  "money": 1000 //송금 금액
}

response
"이체가 완료되었습니다"

회원가입
POST /members
{
  "email": "test@naver.com",
  "password": "12314"
}

친구 추가
POST /members/{memberId}/friends

reqeust
{
  "friendId": 1 //친구 추가할 대상 ID
}

친구 목록
GET /members/{memberId}/friends

response
{
  "id": 1, //친구 ID
  "email": "test@naver.com" //친구 이메일
}
```
## ERD
![스크린샷 2023-03-02 오후 8 39 39](https://user-images.githubusercontent.com/25299428/222419463-35afd0ab-5c57-4e23-85c4-1f3a77cb30fb.png)
### 연관 관계에서 외래키 매핑을 하지 않은 이유
외래키를 매핑하면 무결성은 보장되지만 디비 운영 측면에서 어려운 점들이 많을거라 생각했습니다. 따라서 PK, 유니크 컬럼 등의 변하지 않는 컬럼으로 논리적 매핑했습니다.
### 친구 목록(member_friend), 계좌 테이블(account)에 인덱스를 건 이유
member_id로 non-unique 인덱스를 걸었는데, 일반적으로 친구 목록의 경우 삽입보단 조회가 비중이 더 많고 계좌 테이블도 인덱스 컬럼은 변경되지 않기 때문에 빠른 데이터 조회를 위해 인덱스를 걸었습니다.

## 데이터 정합성을 맞추기 위한 방법
mysql innodb 기준으로 공유락과 배타락이 존재합니다. 공유락은 읽기는 가능하나 쓰기는 불가능하고, 배타락은 쓰기를 위해 레코드를 잠가놓으며 다른 트랜잭션이 읽지 못하게 합니다.
계좌 이체의 경우 1명에게 여러 명이 동시에 이체하는 케이스를 상정하면 업데이트를 위한 하나의 트랜잭션 (select + update) 안에 다른 트랜잭션이 select로 값을 읽어가면 정합성이 깨지는 문제가 존재합니다.
특히 돈은 신뢰성, 실시간성이 중요한 특성을 가지므로 배타락으로 인한 성능 희생을 감수하고 신뢰성을 높이는데 주력하였습니다.
### 왜 DB 잠금을 사용하였나?
redis와 같은 인메모리 기반 DB에서 잠금을 지원하지만 단일 장애점이 될 우려가 있고, 장애 발생 시 디스크에 적재하지 않으면 휘발될 위험이 존재합니다.
그에 반해 DB는 로그 기반으로 디스크에 적재하기 때문에 복구전략이 더 안전할 거라 판단했습니다.
### 스프링에서 구현하기 위한 방법
비관적 락과, 낙관적 락으로 구분되는데 낙관적 락은 충돌이 일어나면 개발자가 직접 롤백처리를 해줘야 하지만 성능이 더 좋고, 비관적 락은 충돌이 일어나면 락을 얻기 위해 대기했다가 수행이 됩니다.
JPA에선 Read Committed 이상의 격리 수준을 적용할 때 둘 중 하나를 선택해야 하며, 계좌 이체는 충돌이 빈번할거라 생각하여 비관적 락을 적용했습니다.
### 테스트 코드에서 검증하기 위한 방법
잔액이 1000원인 1번 유저에게 2~100번의 유저들이 10원씩 보내면 2000원이 되는 경우를 검증하였습니다. 100개의 스레드를 생성하여 동시에 1번 유저에게 계좌 서비스의 이체 메서드를 호출하는 케이스를 테스트한 결과 정상적임을 확인했습니다.

## 알람을 구현한 방법
알람 기능은 다른 도메인과 비교했을 때 응집성은 떨어지지만 필수적으로 수행돼야 합니다. 그러나 이를 계좌 이체 서비스에 넣게 되면, 느슨한 결합을 유지하기 어려워져 유지보수성을 낮추게 됩니다.
따라서 스프링에서 지원하는 기능 중 도메인 이벤트를 사용했습니다. 계좌이체 서비스 내부에서 이벤트를 publish하여 이체 트랜잭션이 커밋되면 이벤트 리스너에서 알람을 보내도록 구현했습니다.
또한 외부 호출 및 알람을 보내기 위한 유저 정보 조회는 관심사 밖의 영역이므로 코루틴을 이용한 비동기로 구현했습니다.
### 알람 트랜잭션에서 예외가 발생한다면?
스프링은 런타임 예외 발생 시 트랜잭션 롤백이 수행됩니다. 계좌 도메인은 알람 도메인에 예외 발생시 계좌 트랜잭션이 영향받지 않아야 합니다. 따라서 트랜잭션 전파 레벨을 REQUIRES_NEW로 하여 별도의 트랜잭션을 만들어 수행되도록 했습니다.
### 왜 코루틴인가?
코루틴은 컨텍스트 스위칭으로 인한 부하가 없고, 프로젝트 특성상 외부 I/O의 비중이 (유저 정보 조회, 외부 호출) 대부분이므로 @Asnyc를 이용한 스레드로 돌리지 않고 코루틴을 채택했습니다.
