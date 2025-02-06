FROM gradle:8.10.2-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /apps

# Gradle 의존성 캐싱 최적화
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
RUN gradle dependencies --no-daemon

# 전체 프로젝트 복사 및 빌드
COPY . .
RUN gradle clean build --no-daemon --parallel -x test

# 실행 환경을 위한 JDK Slim 이미지 사용
FROM openjdk:17-jdk-slim

# JAR 파일 복사 (정확한 파일명 확인 필요)
COPY --from=builder /apps/build/libs/*.jar app.jar

# application.yml 복사 (Spring Boot가 찾을 수 있는 위치로)
COPY src/main/resources/application.yml /config/application.yml

# 포트 노출
EXPOSE 8080

# 컨테이너 실행 시 Java 실행
CMD ["java", "-jar", "/app.jar"]
