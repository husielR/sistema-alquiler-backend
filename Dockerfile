# Etapa 1: Construcción
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
# Compilamos saltando los tests para ir directo al empaquetado
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final de producción
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Solo copiamos el archivo .jar compilado de la etapa anterior
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 9085
ENTRYPOINT ["java", "-jar", "app.jar"]