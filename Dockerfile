FROM maven:3.8.4-openjdk-11 AS buildstage

WORKDIR /app

COPY . .

RUN mvn clean package

FROM openjdk:11.0.11-jre-slim AS runstage

COPY --from=buildstage /app/target/techytax-backend-3.0.6.jar .

EXPOSE 8080

RUN adduser --system --group techytax

USER techytax

ENTRYPOINT ["java", "-jar", "techytax-backend-3.0.6.jar"]
