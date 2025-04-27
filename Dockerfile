# Usa imagem oficial do OpenJDK 21
FROM eclipse-temurin:21-jdk

# Diretório de trabalho
WORKDIR /app

# Copia o jar gerado pelo Maven
COPY target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar o app
ENTRYPOINT ["java", "-jar", "app.jar"]
