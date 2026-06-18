ARG DOCKER_REGISTRY=docker.io

FROM ${DOCKER_REGISTRY}/library/maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .

RUN printf '%s\n' \
    '<?xml version="1.0" encoding="UTF-8"?>' \
    '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">' \
    '  <mirrors><mirror><id>huaweicloud</id><mirrorOf>*</mirrorOf><url>https://repo.huaweicloud.com/repository/maven/</url></mirror></mirrors>' \
    '</settings>' \
    > /tmp/maven-settings.xml

RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -q -s /tmp/maven-settings.xml

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests -q -s /tmp/maven-settings.xml

FROM ${DOCKER_REGISTRY}/library/eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/facility-management-1.0.0.jar app.jar

ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8" \
    LANG=C.UTF-8

EXPOSE 8015

ENTRYPOINT ["java", "-jar", "app.jar"]
