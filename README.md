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
> 주요 기능에 관해서는 블로그에 정리해 링크로 남깁니다.

### 1. JWT 로그인
[JWT 로그인 정리](https://velog.io/@ss412/Spring-Security-JWT-%EB%A1%9C%EA%B7%B8%EC%9D%B8)

### 2. 무한 스크롤


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
