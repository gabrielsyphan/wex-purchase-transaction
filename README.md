# Wex Purchase Transaction
Repository for the Wex project.

## How to run (Recommended)
1. Clone the repository
2. Copy the `.env.example` file to `.env` and fill the variables
3. Run `docker-compose up -d --build` in the root folder
4. Access the Api documentation at `http://localhost:2984/swagger-ui/index.html#/`

### Dependencies
- [Docker](https://docs.docker.com/install/)


## How to run without docker
You can also avoid using docker and run the application directly on your machine. To do so, follow the steps below.

As the application uses MySQL, you need to have it installed and running on your machine, if you don't want to install
it, you can change the `application.yml` file to use an in-memory database like H2 by copying the `application-test.yml` 
content as example.

1. Run `mvn clean install` in the root folder
2. Copy the `.env.example` file to `.env` and fill the variables
3. Run `java -jar target/wex-0.0.1-SNAPSHOT.jar` in the root folder
4. Access the application at `http://localhost:2984/swagger-ui/index.html#/`

### Dependencies
- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Maven](https://maven.apache.org/)
- [MySQL](https://www.mysql.com/)

## Technologies
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [MySQL](https://www.mysql.com/)
- [Java](https://www.java.com/pt_BR/)
- [Spring Boot](https://spring.io/projects/spring-boot)

### Springboot Libraries
- [SpringDoc](https://springdoc.org/)
- [Lombok](https://projectlombok.org/)
- [Maven](https://maven.apache.org/)
- [Caffeine](https://www.baeldung.com/spring-boot-caffeine-cache)
- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [MapStruct](https://mapstruct.org/)
- [SpringSecurity](https://spring.io/projects/spring-security)
- [SpringData](https://spring.io/projects/spring-data)
- [SpringValidation](https://spring.io/guides/gs/validating-form-input/)
- [Cache](https://spring.io/guides/gs/caching/)

## Contributors
- [Gabriel Syphan](https://linkedin.com/in/gabrielsyphan)