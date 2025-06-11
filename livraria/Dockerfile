# Etapa 1: build com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagem final com apenas o .jar
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/livraria-0.0.1-SNAPSHOT.jar app.jar

# Copia o application.yml
COPY src/main/resources/application.yml /app/application.yml

# Executa a aplicação, garantindo que o Spring leia variáveis de ambiente
ENTRYPOINT ["java", "-jar", "app.jar"]
