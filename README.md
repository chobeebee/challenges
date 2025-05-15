#  백엔드 개발 과제 (Java)
> 스파르타 바로인턴
> * 🔗 Swagger UI : http://15.164.97.101:8080/swagger-ui/index.html
> * 🔗 AWS EC2 URL : http://15.164.97.101:8080


## 프로젝트 개요
- 아키텍처 : 모놀리틱 구조 + 레이어드 아키텍처
- 주요 기능 : 회원 가입/로그인, JWT 인증/인가, 권한 분기, 사용자 조회, RestDocs 기반 API 문서화

### 프로젝트 목표

- 인증/인가 처리 흐름 이해
- 테스트와 문서화를 통한 유지보수성 향상
- Spring Security 설정 및 필터 구현

### 기간:
2025년 5월 12일 ~ 2025년 5월 15일

---

## 🛠️ 기술 스택
| 항목     | 기술                                 |
| ------ | ---------------------------------- |
| 언어     | ![JAVA](https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=OpenJDK&logoColor=white)                           |
| 프레임워크  | ![SPRINGBOOT](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white) ![SPRINGSECURITY](https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white) |
| ORM    | ![Spring Data JPA](https://img.shields.io/badge/SpringDataJPA-6DB33F?style=for-the-badge&logo=Hibernate&logoColor=white)                    |
| 인증 방식  | ![JWT](https://img.shields.io/badge/JSONWebToken-000000?style=for-the-badge&logo=JSONWebTokens&logoColor=white)       |
| 테스트/문서 | ![JUnit](https://img.shields.io/badge/JUnit5-4A154B?style=for-the-badge&logo=JUnit5&logoColor=white) ![SpringRestDocs](https://img.shields.io/badge/RestDocs-4169E1?style=for-the-badge&logo=SpringRestDocs&logoColor=white)  |

---

## 구현 기능
☑️ 회원가입 / 로그인 <br>
☑️ JWT 기반 Access/Refresh 토큰 발급 및 검증 <br>
☑️ 토큰 기반 인증/인가 및 역할 분기 <br>
☑️ 사용자 상세 조회 (관리자용) <br>
☑️ 예외 처리 및 공통 응답 포맷 <br>
☑️ 테스트 코드 작성 및 RestDocs 적용

---

## 서비스 구성 및 실행 방법

### 디렉토리 구조
```bash
src
├── main
│   └── java/com/baro/challenges
│       ├── ChallengesApplication.java
│       ├── common/         # 공통 응답, 예외 처리
│       └── user/           # 사용자 도메인
│           ├── config/     # SecurityConfig, 필터 설정
│           ├── contorller/ # 사용자 API 컨트롤러
│           ├── service/    # 사용자 서비스 로직
│           ├── repository/ # JPA 리포지토리
│           ├── dto/        # 요청/응답 DTO
│           ├── entity/     # 유저 엔티티
│           └── jwt/        # JWT 유틸, 필터
└── test/
    └── ...                # RestDocs, 서비스 테스트 등

```

### 실행 방법
#### 1. 저장소 클론
```bash
# 로컬에 저장
git clone https://github.com/chobeebee/challenges.git
cd challenges
```
#### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```
#### 3. 테스트 및 문서화
```bash
./gradlew test
```
> RestDocs는 `build/generated-snippets`에 생성됩니다. Swagger는 `/docs` 경로에서 확인 가능.
