# RTDR — Runtime Threat Detection & Response

```text
==================================================================================
 ⚡ FAHIM UDDIN // RUNTIME THREAT DETECTION SYSTEM (RTDR)
==================================================================================
 [SYSTEM]    RTDR_STANDALONE_ENGINE
 [STATUS]    ALL_SYSTEMS_OPERATIONAL // DEVELOPMENT_AUDIT
 [VERSION]   V2.0.0
 [GATEWAY]   NGINX_REVERSE_PROXY // PORT_80_ENFORCED
 [BACKEND]   SPRING_BOOT_JVM_26 // POSTGRES_18 // REDIS_7_ALPINE
 [KERNEL]    FEDORA_ENGINE // SELINUX_SHARED_LABELING_ACTIVE
==================================================================================
 © 2026 FAHIM UDDIN // CORE_PROTOCOL_V2.0.0 // ALL_RIGHTS_RESERVED
==================================================================================
````

RTDR (Runtime Threat Detection & Response) is an enterprise-grade backend security engine designed for real-time telemetry ingestion, anomaly detection, incident generation, and automated response enforcement across distributed systems.

It operates on a **zero-trust, event-driven architecture**, processing high-frequency telemetry streams and executing real-time mitigation actions.

---

## 🏗️ System Architecture

RTDR is deployed behind an **Nginx reverse proxy layer** exposed on port `80`.
The Spring Boot application is **not directly exposed** to external networks.

### Request Flow

```text
Client → Nginx (Port 80) → Spring Boot (Internal 8080) → PostgreSQL / Redis
```

---

## 🧩 Core Components

| Component   | Role                                       |
| ----------- | ------------------------------------------ |
| Nginx       | Edge gateway / reverse proxy               |
| Spring Boot | Core detection & orchestration engine      |
| PostgreSQL  | Persistent incident & node storage         |
| Redis       | High-speed telemetry + rate tracking cache |

---

## 🚀 Deployment Guide

### 1. Clone Repository

```bash
git clone https://github.com/udnfahim/runtime_threat_detection_rtdr.git
cd runtime_threat_detection_rtdr
```

---

### 2. Environment Configuration

Create local environment file:

```bash
cp .env.example .env
```

Ensure credentials are consistent with Docker Compose:

```env
SPRING_DATASOURCE_USERNAME=rtdr_user
SPRING_DATASOURCE_PASSWORD=rtdr_production_password_string
```

> ⚠️ Important: Password mismatch between `.env` and `docker-compose.yml` will cause authentication failure.

---

### 3. Build & Start Infrastructure

```bash
docker compose up -d --build
```

This initializes:

* Nginx reverse proxy (port 80)
* Spring Boot application (internal port 8080)
* PostgreSQL database
* Redis cache layer

---

### 4. Verify Running Services

```bash
docker compose ps
```

Expected healthy services:

* `rtdr_proxy` → Nginx gateway
* `rtdr_app` → Spring Boot engine
* `rtdr_postgres` → PostgreSQL (healthy)
* `rtdr_redis` → Redis (healthy)

---

## 🩺 Health Check

### ✅ Correct Endpoint (via Nginx Gateway)

```bash
curl http://localhost/actuator/health
```

### ❌ Incorrect (Direct Access Disabled)

```bash
curl http://localhost:8080/actuator/health
```

> Spring Boot is intentionally isolated behind Nginx and not exposed externally.

---

## 📡 API Reference

### 1. Telemetry Ingestion

```bash
curl -X POST http://localhost/api/v1/telemetry \
  -H "Content-Type: application/json" \
  -H "X-Agent-Token: rtdr_agent_telemetry_secure_token_string" \
  -d '{
    "nodeId": "node-fedora-prod-01",
    "sourceIp": "192.168.10.45",
    "subject": "admin_auth_service",
    "failedLogins": 6,
    "requestRate": 142.5,
    "anomalyWeight": 0.85,
    "geoIrregularity": 1.0,
    "windowSeconds": 10
  }'
```

---

### 2. Real-Time Alert Subscription

WebSocket STOMP channel:

```bash
wscat -c ws://localhost/ws-rtdr
```

Subscription frame:

```text
CONNECT
accept-version:1.1,1.2
heart-beat:10000,10000

SUBSCRIBE
id:sub-0
destination:/topic/alerts
```

---

## ⚙️ Internal Service Topology

| Layer                  | Endpoint                             |
| ---------------------- | ------------------------------------ |
| Public Gateway         | [http://localhost](http://localhost) |
| Application (internal) | [http://app:8080](http://app:8080)   |
| Database               | PostgreSQL (Docker network only)     |
| Cache                  | Redis (Docker network only)          |

---

## 🔐 Configuration Notes

### Critical Requirement

All credentials must remain synchronized across:

* `docker-compose.yml`
* `.env`
* `application.yml / properties`

### Example:

```yaml
SPRING_DATASOURCE_PASSWORD: rtdr_production_password_string
```

---

## 🧪 Troubleshooting

### 1. Database Authentication Failure

If logs show:

```text
FATAL: password authentication failed for user "rtdr_user"
```

Reset environment:

```bash
docker compose down -v
docker compose up -d --build
```

---

### 2. Port 8080 Not Accessible

This is expected behavior.
Application is only exposed via Nginx (port 80).

---

## 📦 Development Mode (Optional)

For local backend debugging:

```bash
docker compose up -d postgres redis
```

Then run Spring Boot directly from IDE with:

```
jdbc:postgresql://localhost:5433/rtdr
```

(Requires port mapping in compose if enabled)

---

## 📌 System Design Principles

* Zero-trust architecture
* Internal service isolation
* Event-driven telemetry pipeline
* Sliding-window anomaly detection
* Real-time enforcement via SSE/WebSocket
* Stateless edge gateway routing

---

## ✔ System Status Summary

* Nginx Gateway: Active (port 80)
* Spring Boot Engine: Running (internal only)
* PostgreSQL: Healthy
* Redis: Healthy
* Health Endpoint: Operational via gateway

```

---

If you want next upgrade, I can convert this into:

- **GitHub README with badges + CI/CD section**
- **architecture diagram (PNG/SVG for portfolio)**
- **or a resume-ready project description (very high impact)**
```
