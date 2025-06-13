# Etapa 1: build com Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagem final com apenas o .jar para rodar a aplicação
FROM eclipse-temurin:17-jdk-jammy AS run
WORKDIR /app
COPY --from=build /app/target/livraria-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# Etapa 3: ambiente para rodar testes
FROM maven:3.9.6-eclipse-temurin-17 AS test
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
CMD ["./mvnw", "test"]
