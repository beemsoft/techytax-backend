# 🏦 TechyTax Backend

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-24-blue.svg)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

TechyTax is a powerful, streamlined backend service designed to simplify tax management and financial tracking for freelancers and small businesses. Built on the modern Spring Boot 3 stack, it provides robust security, automated depreciation calculations, and comprehensive fiscal reporting.

---

## ✨ Key Features

- 🔐 **Secure Authentication**: JWT-based stateless authentication with Spring Security 6.
- 📊 **Fiscal Reporting**: Automated Profit & Loss statements and Balance Sheet generation.
- 💸 **Expense Tracking**: Categorized cost management with VAT support.
- 🏗️ **Project Management**: Track customer projects, rates, and revenue percentages.
- 📉 **Automated Depreciation**: Intelligent calculation of asset depreciation over time.
- 📂 **Invoice Management**: Generate and track invoices linked to projects.
- 🐳 **Docker Ready**: Easy deployment with provided `Dockerfile` and `docker-compose.yaml`.

---

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.4.1
- **Language**: Java 24
- **Security**: Spring Security 6 + JJWT
- **Database**: H2 (Embedded for development/testing)
- **Persistence**: Spring Data JPA / Hibernate 6
- **Build Tool**: Maven
- **Lombok**: Reduced boilerplate code

---

## 🚀 Getting Started

### Prerequisites

- **JDK 24** or higher
- **Maven 3.9+**

### Installation & Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/techytax-backend.git
   cd techytax-backend
   ```

2. **Build the project**:
   ```bash
   mvn clean package
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`.

---

## ⚙️ Configuration

### Database
By default, TechyTax uses an embedded **H2** database. The data is persisted in a local file (defined in `application.yml`).

To use another database (e.g., MySQL), update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/techytax
    username: your_user
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
```

### Security
JWT configuration can be found in `application.yml`. **Make sure to change the `jwt.secret` for production environments.**

---

## 🐳 Docker Support

To run TechyTax using Docker:

```bash
docker-compose up --build
```

---

## 🖥️ Frontend

The companion frontend for this backend is built with Angular and can be found here:
👉 **[TechyTax Frontend (techytax-ngx)](https://github.com/beemsoft/techytax-ngx)**

---

## 📜 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---
*Based on the [jwt-spring-security-demo](https://github.com/szerhusenBC/jwt-spring-security-demo).*
