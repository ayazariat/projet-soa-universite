# =========================
# 1) Build stage
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /workspace

# Copier uniquement le pom.xml pour le cache Maven
COPY pom.xml .

RUN mvn -B dependency:go-offline

# Copier tout le code source
COPY src ./src

# Compiler et générer le JAR
RUN mvn -B clean package -DskipTests


# =========================
# 2) Runtime stage
# =========================
FROM eclipse-temurin:21-jre

# Créer un utilisateur non-root
RUN addgroup --system app && adduser --system --ingroup app app

WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=builder /workspace/target/*.jar /app/auth-service.jar

# Port Spring Boot
EXPOSE 8081

# Variables d’environnement
ENV SPRING_PROFILES_ACTIVE=default
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Sécurité : utilisateur non-root
USER app

# Démarrage de l'application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/auth-service.jar"]
