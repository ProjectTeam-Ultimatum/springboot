<p align="center">
  <img src="https://github.com/ProjectTeam-Ultimatum/springboot/assets/159854114/abb2b31c-fb62-44f7-a01f-bc10126bb07a" alt="제주랑 로고">
</p>

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

