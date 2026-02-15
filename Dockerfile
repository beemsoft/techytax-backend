FROM eclipse-temurin:25-jdk-alpine AS buildstage

WORKDIR /app

# Copy the entire project context (including license-shell)
COPY . .

# Install maven (alpine uses apk) and update packages to fix CVEs
RUN apk update && apk upgrade && apk add --no-cache maven

# Run maven package with the 'commercial' profile to include the license-shell
RUN mvn clean package -Pcommercial -DskipTests

# Extract the jar layers
RUN java -Djarmode=layertools -jar target/techytax-backend-4.0.0.jar extract

FROM eclipse-temurin:25-jre-alpine AS runstage

# Update packages to fix CVEs (CVE-2025-24813, CVE-2025-41232)
RUN apk update && apk upgrade && rm -rf /var/cache/apk/*

WORKDIR /app

COPY --from=buildstage /app/dependencies/ ./
COPY --from=buildstage /app/spring-boot-loader/ ./
COPY --from=buildstage /app/snapshot-dependencies/ ./
COPY --from=buildstage /app/application/ ./

EXPOSE 8080

RUN addgroup -S techytax && adduser -S techytax -G techytax

RUN mkdir -p /techytax_data
RUN chown -R techytax:techytax /techytax_data /app
USER techytax

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "org.springframework.boot.loader.launch.JarLauncher"]
