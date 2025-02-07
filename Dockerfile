FROM gradle:8.10.2-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /apps

# 빌더 이미지에서 애플리케이션 빌드
RUN gradle clean build --no-daemon --parallel -x test

FROM openjdk:17-jdk-slim

COPY --from=builder /apps/build/libs/app.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]