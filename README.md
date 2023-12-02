# Shopping_mall

## 1. JWT 로그인

### 1-1. 동작 과정

  Access Token 은 사용자의 email 정보를 포함
 
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


## 2. Exception 처리
