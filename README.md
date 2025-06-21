# 🚦 RateLimitingAPI

A **Spring Cloud Gateway API Gateway** that provides:

- ✅ JWT-based Authentication
- 🚀 Redis-based Dynamic Rate Limiting
- 🌐 Configurable Routes via JPA
- 🛡️ Custom Global Filters for request control

---

## 📌 Features

- 🔐 **JWT Authentication**
  - Secure APIs with JSON Web Tokens
  - `/auth/generateToken` endpoint to generate JWT for services

- ⚙️ **Redis Rate Limiting**
  - Dynamic per-endpoint rate limits stored in Redis
  - Reactive rate limiting filter using Spring Cloud Gateway

- 🌐 **Dynamic API Gateway Routing**
  - Routes defined in DB via `GatewayRoute` entity
  - Load balancing and path rewriting supported

- 🧠 **Global Filters**
  - JWT Authentication filter
  - Redis Rate Limiting filter applied to all requests

---

## 🛠️ Tech Stack

| Layer        | Technology                          |
|--------------|--------------------------------------|
| Backend      | Spring Boot, Spring Cloud Gateway    |
| Auth         | JWT (via `io.jsonwebtoken`)          |
| Rate Limiter | Redis (Reactive)                     |
| Persistence  | Spring Data JPA                      |
| Logging      | SLF4J                                |
| Dev Tools    | Lombok                               |

---

## 🚀 Getting Started

### 🔧 Prerequisites

- Java 17+
- Maven
- Redis (running locally or on cloud)


