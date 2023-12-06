# 쇼핑몰 프로젝트 
> 의류, 잡화 등등 여러 카테고리의 상품을 쇼핑할 수 있는 웹 페이지

## 사용 기술셋
  * Spring Boot 2.7.2
  * MySQL 8.0.33
  * Nginx

## 배포
  * AWS EC2, S3, RDS, Route53

## 동작 예시


## 주요 기능

### 1. JWT 로그인
>  Access Token 은 사용자의 email 정보를 포함
 
  1. 클라이언트는 서버로 JSON 데이터(id, password) 전송
  2. 서버는 검증 후 클라이언트로 Response(Header: Access Token, Cookie: Refresh Token) 전송
  3. 클라이언트는 서버로 요청을 보낼 때, Access Token을 Request Header에 포함하여 전송
  4. 서버는 Access Token 검증(사용자 Email, 만료 기간)
    4-1. 검증 성공 시: 200 or 201 status + body 반환 <br>
    4-2. 검증 실패 시: 401 status + exception 반환  <br>
    4-3. 클라이언트는 Request Header에 Access Token과 Cookie의 Refresh Token을 포함해 재요청 <br>
    4-4. 서버는 Refresh Token을 검증해 유효하다면 201 status + Access Token + Refresh Token 재발급 <br>
    4-5. 클라이언트는 재발급된 Access Token과 함께 재요청 <br>
    4-6. 200 or 201 status + body 반환 <br>
  
 ![2](https://github.com/SudalKing/Shopping_mall/assets/87001865/0729b4dc-dd06-469e-876e-63d2621eb025) <br>
 

### 2. 무한 스크롤
> 

### 3. Exception
> RuntimeException을 상속 받아 서버의 모든 Exception을 관리하는 Class 구조<br>

![image](https://github.com/SudalKing/Shopping_mall/assets/87001865/d934a20c-4028-40e2-8997-c0603f56a706)

<br>

#### 3-1. 패키지 구조

![image](https://github.com/SudalKing/Shopping_mall/assets/87001865/2cb1cee9-2de9-40b7-a6c2-f23e9ce0db9f)

<br>

#### 3-2. 예제 코드
> 최상위 Exception Class를 통해 필요한 도메인에서 상속 받아 원하는 exception을 만들어 사용하고 error code 또한 ErrorCode Class 한 곳에서 관리하며 원하는 도메인의 error code를 직접 만들어 사용<br>

##### BusinessException.java
> 일반적으로 BusinessException을 상속 받아 다양한 Exception 구현

![image](https://github.com/SudalKing/Shopping_mall/assets/87001865/ffb68ff0-f44b-4893-8c7a-c3ba6b0e7557)

##### EntityNotFoundException.java 
> EntityNotFound, InvalidValue 등 자주 발생하는 exception은 도메인 구분없이 공통으로 사용

![image](https://github.com/SudalKing/Shopping_mall/assets/87001865/b384e642-53ee-41ec-8750-18e8b987e064)


##### ErrorCode.java
> 공통으로 사용되는 코드와 도메인 별로 직접 커스텀하여 사용하며 ErrorCode Class 한 곳에서 관리

![image](https://github.com/SudalKing/Shopping_mall/assets/87001865/3f446d49-8974-4d04-8270-d2b102ed2c3f)


## 프로젝트 기간

## 후기
