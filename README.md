# 💡Fligent란?
- Flight와 Agent의 합성어로써, 비행을 도와주는 이를 뜻합니다.

- 공공API를 활용한 국내 주요 공항 내외 실시간 정보제공, 사용자 기반 커뮤니티, 쇼핑몰 서비스를 접목한 종합 플랫폼입니다.

- 국내 공항에 대한 정보는 각 공항페이지로 나뉘어 제공되고 있지만, 한 곳에서 확인할  수 있는 곳은 없어서 만들게 되었습니다.

## 🌐사이트 보러가기
<http://35.79.223.79:8080/fligent>
## ⌛개발기간
2022/09/19 → 2022/11/30
## 🫂팀 구성
FE 1명, BE 3명
## 🔖주요기능
- **Open API를 활용한 국내 주요 공항 내외 정보 · 실시간 비행 스케쥴 조회**
- **사용자간 정보 공유가 가능한 커뮤니티 서비스**
- **비행에 필요한 물품 쇼핑 서비스**
## 🛠️개발스택
**Front-End :** HTML5, CSS3, Vue.js, JavaScript, Element-Plus, Bootstrap, Node.js

**Back-End :** Java, Spring boot, Node.js

**DB :** JPA, Oracle, MyBatis
## 🙋🏻담당업무
**Front-end(기여도 95%)**

- Video.js를 활용한 메인페이지 구성
- 카카오지도 API 사용
    - 메인페이지 · 공항 상세페이지 - 공항 중심 좌표 설정으로 실시간 공항주변 교통 혼잡도 레이어 구현
- 다음 주소 API 사용
    - 주문페이지 - 배송지 주소 조회 · 입력 구현
- 백 - 프론트 연동(REST API)
    - 메인페이지 - 실시간 정보 조회, 인기 게시글 조회, 날씨 정보 조회
    - 공항 상세페이지 - 실시간 정보 조회, 상업시설 조회, 해시태그를 통한 게시판 이동 기능
    - 게시판 - 글쓰기, 검색, 정렬 조회, 상세글 조회, 댓글
    - 회원가입 - 회원가입 기능 구현
    - 로그인 -  로그인, 관리자로그인, 비밀번호 찾기
    - 관리자 - 회원 차단/해제, 상품목록 등록/삭제/수정, 회원게시물 조회/삭제
    - 회원 마이페이지 - 주문목록 조회/취소, 회원정보 수정, 회원 활동글 조회/삭제, 탈퇴, 암호변경
    - 쇼핑몰(+장바구니, 주문, 주문완료) - 장바구니 담기/삭제, 주문
- 컴포넌트 HTML/CSS 적용 · 레이아웃/UI 디자인 (기여도 100%)
