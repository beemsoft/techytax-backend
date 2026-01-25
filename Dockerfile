FROM eclipse-temurin:25-jdk AS buildstage

WORKDIR /app

COPY . .

RUN apt-get update \
    && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

RUN mvn clean package

FROM eclipse-temurin:25-jre AS runstage

COPY --from=buildstage /app/target/techytax-backend-4.0.0.jar .

EXPOSE 8080

RUN adduser --system --group techytax

RUN mkdir -p /techytax_data
RUN chown -R techytax:techytax /techytax_data
USER techytax

ENTRYPOINT ["java", "-jar", "techytax-backend-4.0.0.jar"]
