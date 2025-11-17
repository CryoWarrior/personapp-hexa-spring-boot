FROM maven:3.8.7-eclipse-temurin-11 AS builder

WORKDIR /build

# Copiamos todo el proyecto multi-módulo para que Maven pueda resolver dependencias
COPY . .

# Construimos solo el módulo REST (y sus dependencias) sin tests (repackage se ejecuta en el ciclo)
RUN mvn -pl rest-input-adapter -am clean package -DskipTests

FROM eclipse-temurin:11-jre

WORKDIR /app

# Copiamos el jar generado del módulo REST
COPY --from=builder /build/rest-input-adapter/target/*.jar app.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "app.jar"]


