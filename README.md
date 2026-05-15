# 📅 일정 관리 서비스

Spring Boot 기반 일정 관리 및 공유 서비스입니다.

## 🚀 배포 URL
[https://port-0-schedule-service-mp6lwg83f82d7f03.sel3.cloudtype.app]

## 🛠 기술 스택
- **Backend**: Java 21, Spring Boot, Spring Security, Spring Mail
- **Database**: MySQL (로컬), MariaDB (배포)
- **Frontend**: Thymeleaf, HTML/CSS, FullCalendar
- **Deploy**: Cloudtype, GitHub

## ✨ 주요 기능
- 회원가입 / 로그인 (Spring Security)
- 일정 CRUD (생성/수정/삭제/조회)
- 일정 공유 (이메일 알림 발송)
- 권한 관리 (보기 / 편집)
- 공유받은 일정 표시
- 캘린더 뷰 (FullCalendar)
- 이메일 중복 가입 방지
- 로그인 실패 알림

## 📌 트러블슈팅

**프로파일 분리로 환경별 DB 관리**
- 문제: 로컬(MySQL)과 배포(MariaDB) 환경이 달라 설정 충돌
- 해결: application-local.properties, application-prod.properties로 분리

**일정 삭제 시 외래키 제약 오류**
- 문제: 공유된 일정 삭제 시 schedule_shares 테이블 외래키 오류 발생
- 해결: 일정 삭제 전 연관된 공유 데이터 먼저 삭제 처리

## 🖥 실행 방법
```bash
git clone https://github.com/nuj27301/schedule-service.git
cd schedule-service
# application-local.properties에 DB 정보 입력 후
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```