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


<h2>프로젝트 기간</h2>
<img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8+%EA%B8%B0%EA%B0%84.png" alt="프로젝트 기간">

<h2>팀 소개</h2>

<div align="center">
<img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%B5%9C%ED%9B%84%ED%86%B5%EC%B2%A9.png" alt="팀 소개">
  <h3><strong>Ultimatum</strong></h3>
  <p>우리 '최후통첩팀'은 최종 프로젝트라는 전쟁에서 승리를 목표로 하는 열정적인 6명으로 구성되어 있습니다. <br/>
서로를 배려하며 협업을 통해 첫 승리를 거두고 최종적으로도 승리하겠다는 당찬 포부로 프로젝트에 임하는 사람들입니다.</p>
</div>


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

<h2>기술 스택</h2>
<img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EA%B8%B0%EC%88%A0%EC%8A%A4%ED%83%9D.png" alt="기술 스택">

## 화면 구성 📺

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>메인 페이지</h3>
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%ED%99%88%ED%99%94%EB%A9%B4.png" alt="메인 페이지" style="width: 200px; height: 200px">
    <p>메인 페이지에서는 사용자들이 학습할 수 있는 코스들을 선택할 수 있습니다. 현재 Scratch와 Python 코스가 제공됩니다.</p>
</div>

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>여행정보 페이지</h3>
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%A0%95%EB%B3%B4~.png" alt="소개 페이지" style="width: 200px; max-width: 200px;">
  <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%A0%95%EB%B3%B42.png" alt="소개 페이지" style="width: 200px; max-width: 200px;">
    <p>소개 페이지에서는 Voluntain의 목적과 기능에 대해 설명합니다.</p>
</div>

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>추천코스 페이지</h3>
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%B6%94%EC%B2%9C%EC%BD%94%EC%8A%A4.png" alt="강좌 소개 페이지" style="width: 200px; max-width: 200px;">
    <p>각 강좌의 세부 정보를 제공하며, 사용자는 이 페이지에서 원하는 강좌를 선택하여 학습할 수 있습니다.</p>
</div>


<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>여행메이트 페이지</h3>
    <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EB%A9%94%EC%9D%B4%ED%8A%B81.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
  <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EB%A9%94%EC%9D%B4%ED%8A%B82.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
    <p>강의 영상 페이지에서는 각 강의의 영상을 제공하며, 학습자가 쉽게 강의를 시청하고 따라할 수 있도록 도와줍니다.</p>
</div>


<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>여행후기 페이지</h3>
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%ED%9B%84%EA%B8%B01.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/2024-05-14+17+01+32.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
    <p>강의 영상 페이지에서는 각 강의의 영상을 제공하며, 학습자가 쉽게 강의를 시청하고 따라할 수 있도록 도와줍니다.</p>
</div>

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>일정짜기 페이지</h3>
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B01.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B02.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B03.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B04.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%9D%BC%EC%A0%95%EC%A7%9C%EA%B8%B05.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
    <p>강의 영상 페이지에서는 각 강의의 영상을 제공하며, 학습자가 쉽게 강의를 시청하고 따라할 수 있도록 도와줍니다.</p>
</div>

<div style="border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin-bottom: 16px;">
    <h3>여행후기 페이지</h3>
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%8A%A4%ED%83%80%EC%9D%BC1.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%8A%A4%ED%83%80%EC%9D%BC2.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
      <img src="https://ultimatum-project.s3.ap-northeast-2.amazonaws.com/%EC%97%AC%ED%96%89%EC%8A%A4%ED%83%80%EC%9D%BC3.png" alt="강의 영상 페이지" style="width: 200px; max-width: 200px;">
    <p>강의 영상 페이지에서는 각 강의의 영상을 제공하며, 학습자가 쉽게 강의를 시청하고 따라할 수 있도록 도와줍니다.</p>
</div>
