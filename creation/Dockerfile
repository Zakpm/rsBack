# Utiliser l'image de base OpenJDK
FROM openjdk:17-alpine

# Copier le fichier JAR de votre application Spring Boot dans l'image Docker
COPY target/creation-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port sur lequel votre application s'exécute (généralement 8080 pour Spring Boot)
EXPOSE 9981

# Variable d'environnement 
ENV SPRING_PROFILES_ACTIVE=prod

# Commande pour exécuter votre application Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]

