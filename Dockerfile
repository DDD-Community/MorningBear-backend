FROM eclipse-temurin:11
LABEL maintainer="yoonho"
WORKDIR /home/deploy/morningbear

ENV TZ="Asia/Seoul"
ENV LC_ALL=C.UTF-8
#ENV GC_OPTS="-XX:+UseG1GC -XX:MinRAMPercentage=50.0 -XX:MaxRAMPercentage=80.0

COPY ./build/libs/*SNAPSHOT.jar /app.jar

#ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=deploy -jar /app.jar"]

# (2023.01.23 yoonho) MySQL 서버가 Shutdown되는 이슈로 우선 H2에 연결
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=default -jar /app.jar"]