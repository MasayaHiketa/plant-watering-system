## 📘Plant Watering Management System(持續開發中)

> A system for managing watering and fertilizing schedules of houseplants.  
> Built with **Spring Boot + PostgreSQL + Docker + GitHub Actions + AWS EC2**, this project demonstrates a practical DevOps-integrated Java backend service.

---

## 📘 Overview
This system helps users keep track of their plants' watering cycles.  
Each user can manage their own plant collection, receive schedule reminders, and review past watering logs.

## 📘Architecture
```text
┌──────────────────────────┐
│      Angular UI (S3 + CloudFront)      │
│  └── Displays plant dashboard, calendar│
└──────────────────────────┘
             │ REST API
┌──────────────────────────┐
│ Spring Boot (EC2 / ECS)│
│  ├─ Plant CRUD            │
│  ├─ Watering Scheduler    │
│  ├─ Webhook Notification  │
│  └─ JWT-based Auth        │
└──────────────────────────┘
             │
┌──────────────────────────┐
│ PostgreSQL (RDS)         │
│  ├─ plants               │
│  ├─ watering_logs        │
│  └─ users                │
└──────────────────────────┘
````

---

## 📘 Tech Stack

**Backend**

- Java 17 / Spring Boot 3
- Spring Data JPA / Spring Security / Flyway
- PostgreSQL

**DevOps / Cloud**

- Docker / Docker Compose
- GitHub Actions (CI/CD pipeline)
- AWS EC2
- 之後預計：ECS / ECR / RDS / CloudFront

## 📘 Example API Endpoints

| Method | Endpoint | Description |
|--------|-----------|-------------|
| GET | `/api/plants` | Get all plants for current user |
| POST | `/api/plants` | Create plant |
| POST | `/api/plants/{id}/water` | Water a plant |
| GET | `/api/plants/{id}/logs` | Get watering logs |
| GET | `/api/plants/due-today` | Get plants due today |
| GET | `/api/admin/webhook-errors` | Admin webhook outbox list |



---

