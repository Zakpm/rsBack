# Utiliser l'image de base OpenJDK
FROM openjdk:17-alpine

# Copier les fichiers JAR de chaque application Spring Boot dans l'image Docker
COPY target/contact-0.0.1-SNAPSHOT.jar /app/contact.jar
COPY target/creation-0.0.1-SNAPSHOT.jar /app/creation.jar
COPY target/security-0.0.1-SNAPSHOT.jar /app/security.jar

# Exposer les ports sur lesquels chaque application s'exécute
EXPOSE 9981 9982 9983

# Variable d'environnement pour chaque application (si nécessaire)
ENV SPRING_PROFILES_ACTIVE_contact=prod
ENV SPRING_PROFILES_ACTIVE_creation=prod
ENV SPRING_PROFILES_ACTIVE_security=prod

# Commande pour démarrer chaque application Spring Boot
CMD java -jar /app/contact.jar & java -jar /app/creation.jar & java -jar /app/security.jar

