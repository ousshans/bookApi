# Book API

## Summary
Mini-projet Spring Boot pour gérer une collection de livres et d’auteurs, avec :  
– CRUD livres & auteurs  
– Recherche par ISBN via OpenLibrary  
– Note d’un livre (recency + popularité auteur)  
– Publication d’événements “author.news” sur NATS  
– Filtre `api-key` sur tous les endpoints (`/api/*`)

## Technologies
- Java 21, Spring Boot 3
- Spring Data JPA (H2 en local / PostgreSQL en prod)
- MapStruct (DTO ↔ Entity)
- NATS (jnats client)
- SLF4J / Logback
- JUnit 5, Mockito

## Installer & lancer NATS
1. **Avec Docker**
   ```bash
   docker run -d --name nats-server -p 4222:4222 nats
2.   **Subcscribe to author news pour voir les events**
   ```bash
   nats sub author.news
```

## Lancer l'application
```bash
   mvn clean spring-boot:run
```