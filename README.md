## Introduction

TechyTax backend is based on [jwt-spring-security-demo](https://github.com/szerhusenBC/jwt-spring-security-demo).

## Requirements

This application is built with Maven and Java 11.

## Usage

Just start the application with the Spring Boot maven plugin (`mvn spring-boot:run`). The application is
running at [http://localhost:8080](http://localhost:8080).

### Using another database

Actually this demo is using an embedded H2 database that is automatically configured by Spring Boot. If you want to connect to another database you have to specify the connection in the *application.yml* in the resource directory. Here is an example for a MySQL DB:

```
spring:
  jpa:
    hibernate:
      # possible values: validate | update | create | create-drop
      ddl-auto: create-drop
  datasource:
    url: jdbc:mysql://localhost/myDatabase
    username: myUser
    password: myPassword
    driver-class-name: com.mysql.jdbc.Driver
```

*Hint: For other databases like MySQL sequences don't work for ID generation. So you have to change the GenerationType in the entity beans to 'AUTO' or 'IDENTITY'.*

## Copyright and license

The code is released under the [MIT license](LICENSE?raw=true).
