```markdown
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

```

RTDR (Runtime Threat Detection & Response) is an enterprise-grade, ultra-low-latency backend security engine designed to safeguard modern infrastructure topologies. The system ingests high-frequency telemetry data streams from remote runtime agents, correlates distributed event indicators, evaluates multi-variable risk matrices in real-time, broadcasts live security alerts over persistent socket layers, and pushes active mitigation enforcement instructions back to edge nodes.

---

## 🏗️ Architectural Topology & Core Flows

The platform enforces a strict **Zero-Trust Infrastructure Perimeter**. Relational storage layers and in-memory transactional cache mirrors are completely isolated within a private internal network bridge (`rtdr_network`), eliminating unauthorized external port binding risks. All inbound public data and agent traffic pass exclusively through a security-hardened Nginx reverse proxy gateway running on standard web boundaries.

```text
                        [ AGENT / CLIENT INGRESS ]
                                    │
                                    ▼ (Port 80)
                       ┌─────────────────────────┐
                       │    Nginx Edge Proxy     │ [Hardened Security Headers]
                       │ (conf.d/default.conf)   │ [Frame Blockades Active]
                       └────────────┬────────────┘
                                    │
       ┌────────────────────────────┴────────────────────────────┐ (Private Bridge Mesh)
       │ (HTTP REST APIs)                                        │ (WebSocket Alerts / SSE)
       ▼ (Internal Port 8080)                                    ▼
┌────────────────────────────────────────────────────────────────────────────────────────┐
│                                SPRING BOOT 4.0.6 ENGINE                                │
│                                                                                        │
│  ┌────────────────────────┐    ┌────────────────────────┐    ┌──────────────────────┐  │
│  │     NodeController     │    │   TelemetryController  │    │    AlertDispatcher   │  │
│  └───────────┬────────────┘    └───────────┬────────────┘    └───────────┬──────────┘  │
│              │ (Registration)              │ (Telemetry Ingest)          │ (WS / STOMP)│
│              ▼                             ▼                             ▼             │
│  ┌────────────────────────┐    ┌────────────────────────┐    ┌───────────┴──────────┐  │
│  │       NodeService      │    │   BruteForceDetector   │    │      /topic/alerts   │  │
│  └───────────┬────────────┘    └───────────┬────────────┘    └──────────────────────┘  │
│              │                             │ (In-Memory Counters)                      │
│              │                             ▼                                           │
│              │                 ┌────────────────────────┐                              │
│              │                 │   RiskScoringEngine    │                              │
│              │                 └───────────┬────────────┘                              │
│              │                             │ (Threshold Breach)                        │
│              ▼                             ▼                                           │
│  ┌──────────────────────────────────────────────────────┐    ┌──────────────────────┐  │
│  │               EnforcementService (SSE)               │◄───┤   Incident Creation  │  │
│  └───────────────────────────┬──────────────────────────┘    └───────────┬──────────┘  │
└──────────────────────────────┼───────────────────────────────────────────┼─────────────┘
                               │ (Auto-Block / SSE Stream)                 │
                               ▼                                           ▼
                    ┌────────────────────┐                      ┌────────────────────┐
                    │     Redis 7.4      │                      │   PostgreSQL 18    │
                    │ (Transient Expiry) │                      │ (Persistent Store) │
                    └────────────────────┘                      └────────────────────┘

```

### Dynamic Execution Mechanics

1. **Agent Registration:** Host machines deploy an on-metal runtime daemon that registers itself via `POST /api/v1/nodes/register`. The `NodeService` persists the instance metadata and grants operational validation states.
2. **Telemetry Ingestion:** Validated agents stream system analytics payloads down to `POST /api/v1/telemetry`.
3. **Sliding-Window Volumetric Analysis:** The `BruteForceDetector` parses transaction hashes, creates an identity grouping inside **Redis** via compound tracking keys, increments dynamic rate metrics against a configurable `failed_logins_threshold`, and applies a sliding TTL window eviction strategy.
4. **Weighted Anomaly Correlator:** The `RiskScoringEngine` processes incoming telemetry streams against an additive metric risk matrix:

$$\text{Risk Score} = \text{failedLogins} + \text{requestRate} + \text{anomalyWeight} + \text{geoIrregularity}$$



If the compiled score breaches the globally set `malicious_threshold`, a persistent relational `Incident` record is generated.
5. **Asynchronous Alert Broadcast:** The `AlertDispatcher` encapsulates the threat context into optimized JSON payloads and broadcasts it immediately to active listening clients over a STOMP pipeline target at `/topic/alerts`.
6. **Reactive Command Enforcement:** Edge agents run an active, non-blocking Server-Sent Events (SSE) socket open at `/api/v1/telemetry/enforcement/stream/{nodeId}`. Upon high-severity incident generation, the `EnforcementService` pushes automatic reactive instruction payloads (e.g., `auto_block`) down the pipeline to execute on the local agent.

---

## 🛠️ Advanced Tech Stack Spec Matrix

* **Execution Runtime:** Java 25 (Maven Tooling Build Spec) // Eclipse Temurin JDK 26 (Distroless Container Topologies).
* **Core Framework:** Spring Boot 4.0.6 (Spring Framework 7.0 Deep Reactive Infrastructure).
* **Perimeter Security:** Spring Security engine configured with decoupled JWT Token Resource Evaluation, backed by an unprivileged Nginx edge reverse-proxy tier.
* **Streaming Inter-Process Pipes:** Spring WebSocket STOMP Messaging Engine + HTML5 Server-Sent Events (SSE) Pipeline.
* **Storage Matrix:** PostgreSQL 18 Cluster (Persistent Domain State Layer) + Redis 7 Alpine Caching Rings (Transient In-Memory Evaluation Rings).
* **Schema Evolution:** Automated Flyway Migrations running sequential structural execution updates on boot.

---

## 🚀 Orchestration Runbook

Follow this precise sequence to deploy the entire multi-tier cluster environment inside your host machine.

### 1. Replicate the Workspace Environment

```bash
git clone [https://github.com/udnfahim/runtime_threat_detection_rtdr.git](https://github.com/udnfahim/runtime_threat_detection_rtdr.git)
cd runtime_threat_detection_rtdr

```

### 2. Isolate Configuration Parameters

Instantiate your localized production and container orchestration variables using the deployment template:

```bash
cp .env.example .env

```

*(Review and audit `.env` to verify your local storage engine passwords align before initialization).*

### 3. Binary Compilation Execution

Compile your source code and extract your hardened, self-contained Spring Boot run-executable artifact:

```bash
./mvnw clean package -DskipTests

```

### 4. Spawning the Infrastructure Grid

Orchestrate and start up your reverse-proxy edge layer, backend microservice instance, memory-ring cache, and persistent engine into the background bridge:

```bash
docker compose up -d --build

```

### 5. Verified Operational Access Points

* **Hardened Edge Gateway Ingress:** `http://localhost:80`
* **Real-Time Telemetry Socket Engine:** `ws://localhost/ws-rtdr`
* **Operational Telemetry Hub:** `http://localhost/actuator/health`

---

## 🧪 Real-World API Execution & Validation Specs

### 1. Streaming Agent Telemetry (`POST /api/v1/telemetry`)

To feed data into the evaluation engine, execute a POST request containing a structured telemetry metric payload. The ingestion pipe expects the secure identification header parameter:

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

### 2. Subscribing to Live Real-Time Alerts

To evaluate structural socket output when incident bounds are tripped, connect to the streaming cluster endpoint via standard WebSocket clients or terminal tools (e.g., `wscat`):

```bash
# Establish connection to the engine socket core
wscat -c ws://localhost/ws-rtdr

# Send standard STOMP frame hook subscription
CONNECT
accept-version:1.1,1.2
heart-beat:10000,10000

SUBSCRIBE
id:sub-0
destination:/topic/alerts

```

---

## ⚙️ Local Development & Debugging Workflow

To easily modify and hot-reload code logic inside an IDE like IntelliJ without waiting for container rebuild sequences:

1. Map the internal PostgreSQL storage node out to your local system loopback inside `docker-compose.yml`:
```yaml
postgres:
  ports:
    - "5433:5432"

```


2. Spawn only the backing data services via your host shell system:
```bash
docker compose up -d postgres redis

```


3. Launch your primary engine class `RuntimeThreatDetectionRtdrApplication.java` inside your local IDE setup. The environment links up using HikariCP directly targeting port `5433`.

---

## ⚠️ Pre-Production Engineering Checklist

Before pushing this system into a live operational cloud deployment, the following architecture mismatches and bugs **must be addressed** in the codebase:

* [ ] **Infrastructure Parameter Sync:** The application configuration currently maps its database connection to password string `rtdr_pass`, but the PostgreSQL container creation spec forces `rtdr_production_password_string`. Synchronize these inside your localized `.env` configuration.
* [ ] **Flyway Relational Realignment:** The `V1__init_rtdr_schema.sql` migration file is missing several columns currently mapped inside the Java JPA layer. Update the SQL migration code to explicitly add:
* `rule_type`, `threshold`, and `is_active` inside the `policies` table definition.
* `os_version` inside the `nodes` table definition.


* [ ] **Ingest Null Pointer Mitigation:** In `RiskScoringEngine.java`, evaluating telemetry streams containing missing or invalid node cross-references will throw a `NullPointerException` during the high-risk branch check on `node.getId()`. Wrap this validation block in an explicit `Optional` check or structural null-guard statement.
* [ ] **Layer Decoupling Optimization:** `TelemetryController.java` bypasses the existing intermediate application layer entirely, making direct orchestration calls to `BruteForceDetector` and `RiskScoringEngine`. Refactor traffic to route through `TelemetryService.java` to comply with clean three-tier architecture principles.

```

```
