````markdown
# 🌿 Plant Watering Management System

> A full-stack cloud-based system for managing watering and fertilizing schedules of houseplants.  
> Built with **Spring Boot + PostgreSQL + AWS + Docker + GitHub Actions**, this project demonstrates a practical DevOps-integrated Java backend service.

---

## 🌱 Overview
This system helps users keep track of their plants' watering and fertilizing cycles.  
Each user can manage their own plant collection, receive schedule reminders, and review past watering logs.

### 🧩 Key Features
| Category | Description |
|-----------|--------------|
| 🌿 **Plant Management** | CRUD operations for plants (name, type, watering/fertilizing intervals). |
| 💧 **Watering Log** | Records every watering/fertilizing event automatically. |
| ⏰ **Smart Scheduler** | Detects when a plant needs watering and triggers notifications (via webhook). |
| 👥 **Multi-user Support** | Each user has independent schedules and data. |
| ☁️ **Cloud Infrastructure** | Deployed on AWS ECS (Fargate) + RDS + S3 + CloudFront. |
| ⚙️ **DevOps Automation** | GitHub Actions for CI/CD: build → test → deploy automatically. |

---

## 🏗️ Architecture
```text
┌──────────────────────────┐
│      Angular UI (S3 + CloudFront)      │
│  └── Displays plant dashboard, calendar│
└──────────────────────────┘
             │ REST API
┌──────────────────────────┐
│ Spring Boot (ECS/Fargate)│
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

## 🧰 Tech Stack

**Backend**

* Java 17 / Spring Boot 3
* Spring Data JPA / Spring Security / Flyway
* PostgreSQL (RDS)

**Frontend**

* Angular 17 (TypeScript)
* FullCalendar / Responsive UI / REST API Integration

**DevOps / Cloud**

* Docker / Docker Compose
* GitHub Actions (CI/CD pipeline)
* AWS ECS (Fargate) + RDS + CloudWatch
* AWS S3 + CloudFront (frontend hosting)

---

## 🚀 Deployment Pipeline (CI/CD)

| Step                   | Description                                        |
| ---------------------- | -------------------------------------------------- |
| 🧪 **Test**            | Run Maven tests & static checks on GitHub Actions. |
| 🐳 **Build**           | Build Docker image & push to AWS ECR.              |
| ☁️ **Deploy**          | Update ECS service automatically (zero downtime).  |
| 🌐 **Frontend Deploy** | Angular build artifacts → AWS S3 → CloudFront.     |

---

## 🖥️ Live Demo

> 🔗 [Try Demo](https://plant.masaya.dev)
> *(Each user manages their own watering schedule.)*

---

## 📘 Example API Endpoints

| Method | Endpoint                 | Description                     |
| ------ | ------------------------ | ------------------------------- |
| `GET`  | `/api/plants`            | Get all plants for current user |
| `POST` | `/api/plants`            | Add new plant                   |
| `POST` | `/api/plants/{id}/water` | Record watering                 |
| `GET`  | `/api/watering-logs`     | View watering history           |

---

## 🧠 Future Extensions

* 🌸 PWA version (offline access)
* 📅 Smart calendar with watering frequency prediction
* 🪴 AI recommendation for optimal watering schedule
* 🌍 Multi-language UI (EN / ZH / JA)

---

## 👤 Author

**Masaya Hiketa**
* ✉️ [macshaiy0a807@gmail.com](mailto:macshaiy0a807@gmail.com)
---


---

