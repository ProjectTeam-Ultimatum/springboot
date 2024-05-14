<h2>프로젝트 소개</h2>

<h1 align="center">여행 추천 플랫폼 "제주랑"</h1>

<p align="center">
  사용자의 여행 MBTI 스타일에 맞춰서 맞춤 여행 추천 서비스를 제공! <br/>
  관심사 또는 여행스타일이 같은 사람들과의 채팅을 통한 매칭서비스! <br/>
  여행을 떠나기전 간편하게 계획을 짜보실 수 있습니다.<br/>
</p>

<p align="center">
  <img src="https://github.com/ProjectTeam-Ultimatum/springboot/assets/159854114/abb2b31c-fb62-44f7-a01f-bc10126bb07a" alt="제주랑 로고">
</p>

---

<h2>프로젝트 기간</h2>
<img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8+%EA%B8%B0%EA%B0%84.png" alt="프로젝트 기간">

---

<h2>팀 소개</h2>

<div align="center">
<img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%B5%9C%ED%9B%84%ED%86%B5%EC%B2%A9.png" alt="팀 소개">
  <h3><strong>Ultimatum</strong></h3>
  <p>우리 '최후통첩팀'은 최종 프로젝트라는 전쟁에서 승리를 목표로 하는 열정적인 6명으로 구성되어 있습니다. <br/>
서로를 배려하며 협업을 통해 첫 승리를 거두고 최종적으로도 승리하겠다는 당찬 포부로 프로젝트에 임하는 사람들입니다.</p>
</div>

---

<h2>팀원 소개</h2>
</div> 

* **배정현**: 팀장/풀스택 개발
* **이은재**: 백엔드 팀장/백엔드 개발
* **김이랑**: 프론트 팀장/풀스택 개발
* **이강권**: 백엔드 개발/데이터베이스 설계
* **김루아**: 풀스택 개발/게시판,소셜로그인 구현
* **최준영**: 풀스택 개발/코스 스케줄 구현

<div align="center">
<img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%ED%8C%80%EC%9B%90%EC%86%8C%EA%B0%9C1.png" alt="팀원소개">
</div>

---

<h2>시작 가이드</h2>

- DB 계정 생성
```
mysql -u root -p
```
```
USE mysql;
```
```
create user 'ultimatum'@'%' identified by '1234';
```
```
CREATE DATABASE ultimatum_db;
```
```
GRANT ALL PRIVILEGES ON ultimatum_db.* TO 'ultimatum'@'%';
```
- 설치 및 실행
  
```
Gradle > Tasks > other > copyGitSubmodule
```

---


<h2>기술 스택</h2>
<img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EA%B8%B0%EC%88%A0%EC%8A%A4%ED%83%9D.png" alt="기술 스택">

---

## 화면 구성 📺

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>메인 페이지</h3>
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EB%A9%94%EC%9D%B8%ED%8E%98%EC%9D%B4%EC%A7%802.png" alt="소개 페이지" style="width: 350px; max-width: 350px;">
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EB%A9%94%EC%9D%B8%ED%8E%98%EC%9D%B4%EC%A7%803.png" alt="소개 페이지" style="width: 350px; max-width: 350px;">
</div>

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>여행정보 페이지</h3>
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%A0%95%EB%B3%B4~.png" alt="소개 페이지" style="width: 350px; max-width: 350px;">
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%A0%95%EB%B3%B42.png" alt="소개 페이지" style="width: 350px; max-width: 350px;">
</div>

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>추천코스 페이지</h3>
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%B6%94%EC%B2%9C%EC%BD%94%EC%8A%A4.png" alt="강좌 소개 페이지" style="width: 350px; max-width: 350px;">
</div>


<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>여행메이트 페이지</h3>
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EB%A9%94%EC%9D%B4%ED%8A%B81.png" alt="강의 영상 페이지" style="width: 350px; max-width: 350px;">
  <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EB%A9%94%EC%9D%B4%ED%8A%B82.png" alt="강의 영상 페이지" style="width: 350px; max-width: 350px;">
</div>


<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>여행후기 페이지</h3>
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%ED%9B%84%EA%B8%B01.png" alt="강의 영상 페이지" style="width: 350px; max-width: 350px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/2024-05-14+17+01+32.png" alt="강의 영상 페이지" style="width: 350px; max-width: 350px;">
</div>

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>일정짜기 페이지</h3>
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B01.png" alt="강의 영상 페이지" style="width: 300px; max-width: 300px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B02.png" alt="강의 영상 페이지" style="width: 300px; max-width: 300px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B03.png" alt="강의 영상 페이지" style="width: 300px; max-width: 300px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B04.png" alt="강의 영상 페이지" style="width: 300px; max-width: 300px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B05.png" alt="강의 영상 페이지" style="width: 300px; max-width: 300px;">
</div>

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>나의 여행 스타일 페이지</h3>
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%8A%A4%ED%83%80%EC%9D%BC1.png" alt="강의 영상 페이지" style="width: 300px; max-width: 300px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%8A%A4%ED%83%80%EC%9D%BC2.png" alt="강의 영상 페이지" style="width: 300px; max-width: 300px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%8A%A4%ED%83%80%EC%9D%BC3.png" alt="강의 영상 페이지" style="width: 300px; max-width: 300px;">
</div>


---


## 주요 기능 📦

### ⭐️ 여행 정보 추천
- 광고에서 보는 맛집이 아닌 "제주랑"에서 엄선한 알짜배기 정보들을 추천!
- 카테고리 및 태그 등으로 내가 원하는 정보를 간편하게 찾아서 볼 수 있는 필터기능 
### ⭐️ 여행 메이트 찾기
- 여행스타일이 맞는 사람들을 찾아 볼 수 있어요
- 채팅으로 충분한 대화를 하고 만남을 가져볼 수 있어요
- 신고와 비속어 필터 기능으로 안전하게 이용할 수 있어요
### ⭐️ 여행 스타일
- 나만의 여행스타일을 MBTI로 재밌게 알아볼 수 있어요
### ⭐️ 추천 코스
- "제주랑"이 추천하는 테마별 코스를 한눈에 알아볼 수 있어요
### ⭐️ 여행 후기
- 여행을 다녀온 사용자들의 후기를 볼 수 있어요
- 원하는 지역과 제목을 검색해서 볼 수 있는 필터링 기능이 있어요
- 댓글과 좋아요 기능으로 다른사람들과 소통할 수 있어요
### ⭐️ 일정 짜기
- 내가 원하는 날짜, 기간 등을 설정하고 장소를 선택하여 나만의 코스를 짜볼 수 있어요


---


## 아키텍처 

### 디렉토리 구조
```
C:.
├───.github
│   └───ISSUE_TEMPLATE
├───.gradle
│   ├───8.7
│   │   ├───checksums
│   │   ├───dependencies-accessors
│   │   ├───executionHistory
│   │   ├───expanded
│   │   ├───fileChanges
│   │   ├───fileHashes
│   │   └───vcsMetadata
│   ├───buildOutputCleanup
│   └───vcs-1
├───.idea
│   └───modules
├───build
│   ├───classes
│   │   └───java
│   │       └───main
│   │           └───ultimatum
│   │               └───project
│   │                   ├───controller
│   │                   │   └───recommend
│   │                   ├───domain
│   │                   │   ├───dto
│   │                   │   │   ├───chatDTO
│   │                   │   │   ├───event
│   │                   │   │   ├───food
│   │                   │   │   ├───hotel
│   │                   │   │   ├───image
│   │                   │   │   ├───logInDTO
│   │                   │   │   ├───place
│   │                   │   │   ├───plan
│   │                   │   │   ├───recommendReply
│   │                   │   │   │   ├───event
│   │                   │   │   │   ├───food
│   │                   │   │   │   ├───hotel
│   │                   │   │   │   └───place
│   │                   │   │   ├───reviewDTO
│   │                   │   │   └───reviewReplyDTO
│   │                   │   └───entity
│   │                   │       ├───chat
│   │                   │       ├───event
│   │                   │       ├───food
│   │                   │       ├───hotel
│   │                   │       ├───image
│   │                   │       ├───Map
│   │                   │       ├───member
│   │                   │       ├───place
│   │                   │       ├───plan
│   │                   │       ├───question
│   │                   │       ├───recommendReply
│   │                   │       └───review
│   │                   ├───global
│   │                   │   ├───config
│   │                   │   │   ├───mail
│   │                   │   │   ├───modelmapper
│   │                   │   │   ├───s3
│   │                   │   │   ├───Security
│   │                   │   │   │   ├───auth
│   │                   │   │   │   └───jwt
│   │                   │   │   ├───swagger
│   │                   │   │   ├───util
│   │                   │   │   └───websocket
│   │                   │   └───exception
│   │                   ├───handler
│   │                   ├───map
│   │                   ├───repository
│   │                   │   ├───chat
│   │                   │   ├───event
│   │                   │   ├───food
│   │                   │   ├───hotel
│   │                   │   ├───image
│   │                   │   ├───member
│   │                   │   ├───place
│   │                   │   ├───plan
│   │                   │   ├───reply
│   │                   │   └───review
│   │                   └───service
│   │                       ├───chat
│   │                       ├───member
│   │                       ├───recommned
│   │                       ├───review
│   │                       └───S3
│   ├───generated
│   │   └───sources
│   │       ├───annotationProcessor
│   │       │   └───java
│   │       │       └───main
│   │       └───headers
│   │           └───java
│   │               └───main
│   ├───resources
│   │   └───main
│   │       ├───static
│   │       │   ├───css
│   │       │   └───images
│   │       └───templates
│   └───tmp
│       └───compileJava
│           └───compileTransaction
│               ├───backup-dir
│               └───stash-dir
├───gradle
│   └───wrapper
├───src
│   ├───main
│   │   ├───java
│   │   │   └───ultimatum
│   │   │       └───project
│   │   │           ├───controller
│   │   │           │   └───recommend
│   │   │           ├───domain
│   │   │           │   ├───dto
│   │   │           │   │   ├───chatDTO
│   │   │           │   │   ├───event
│   │   │           │   │   ├───food
│   │   │           │   │   ├───hotel
│   │   │           │   │   ├───image
│   │   │           │   │   ├───logInDTO
│   │   │           │   │   ├───place
│   │   │           │   │   ├───plan
│   │   │           │   │   ├───recommendReply
│   │   │           │   │   │   ├───event
│   │   │           │   │   │   ├───food
│   │   │           │   │   │   ├───hotel
│   │   │           │   │   │   └───place
│   │   │           │   │   ├───reviewDTO
│   │   │           │   │   └───reviewReplyDTO
│   │   │           │   └───entity
│   │   │           │       ├───chat
│   │   │           │       ├───event
│   │   │           │       ├───food
│   │   │           │       ├───hotel
│   │   │           │       ├───image
│   │   │           │       ├───Map
│   │   │           │       ├───member
│   │   │           │       ├───place
│   │   │           │       ├───plan
│   │   │           │       ├───question
│   │   │           │       ├───recommendReply
│   │   │           │       └───review
│   │   │           ├───global
│   │   │           │   ├───config
│   │   │           │   │   ├───mail
│   │   │           │   │   ├───modelmapper
│   │   │           │   │   ├───s3
│   │   │           │   │   ├───Security
│   │   │           │   │   │   ├───auth
│   │   │           │   │   │   └───jwt
│   │   │           │   │   ├───swagger
│   │   │           │   │   ├───util
│   │   │           │   │   └───websocket
│   │   │           │   └───exception
│   │   │           ├───handler
│   │   │           ├───map
│   │   │           ├───repository
│   │   │           │   ├───chat
│   │   │           │   ├───event
│   │   │           │   ├───food
│   │   │           │   ├───hotel
│   │   │           │   ├───image
│   │   │           │   ├───member
│   │   │           │   ├───place
│   │   │           │   ├───plan
│   │   │           │   ├───reply
│   │   │           │   └───review
│   │   │           └───service
│   │   │               ├───chat
│   │   │               ├───member
│   │   │               ├───recommned
│   │   │               ├───review
│   │   │               └───S3
│   │   └───resources
│   │       ├───static
│   │       │   ├───css
│   │       │   └───images
│   │       └───templates
│   └───test
│       └───java
│           └───ultimatum
│               └───project
└───Submodule

```
