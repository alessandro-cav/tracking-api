# Etapa 1: Construir o projeto usando uma imagem Maven com JDK 17
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copie os arquivos de configuração do Maven
COPY pom.xml .
# Baixe as dependências do projeto
RUN mvn dependency:go-offline

# Copie o restante do código fonte
COPY src ./src
# Compile o código e crie o artefato JAR
RUN mvn clean package -DskipTests

# Etapa 2: Executar o projeto usando uma imagem OpenJDK 17
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Porta em que a aplicação irá rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
