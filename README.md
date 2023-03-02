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
