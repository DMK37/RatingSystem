FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy Maven configuration files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Build dependencies layer (leverages Docker cache)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build application
RUN ./mvnw package -DskipTests

RUN find /app/target -name "*.jar" -not -name "*sources.jar" -not -name "*javadoc.jar" -type f -exec cp {} /app/app.jar \; && \
    ls -la /app/

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]