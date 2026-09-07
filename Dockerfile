# Etapa 1: Construccion
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .

# Damos permiso de ejecucion al archivo de Maven
RUN chmod +x mvnw

# Compilamos saltando los tests (porque GitHub Actions ya los ejecuto antes)
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final de produccion
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Solo copiamos el archivo .jar compilado de la etapa anterior
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 9085
ENTRYPOINT ["java", "-jar", "app.jar"]