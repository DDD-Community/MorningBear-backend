FROM eclipse-temurin:11
LABEL maintainer="yoonho"
WORKDIR /home/deploy/morningbear

ENV TZ="Asia/Seoul"
ENV LC_ALL=C.UTF-8
ENV GC_OPTS="-XX:+UseG1GC -XX:MinRAMPercentage=50.0 -XX:MaxRAMPercentage=80.0

COPY ./build/libs/*SNAPSHOT.jar /app.jar

ENTRYPOINT ["sh", "-c", "java $GC_OPTS -jar /app.jar"]