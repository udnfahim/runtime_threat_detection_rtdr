RTDR - Runtime Threat Detection & Response

Quick start (requires Docker & Maven):

1. Copy env: cp .env.example .env and edit values.
2. Build app jar: mvn -DskipTests clean package
3. Start infra and app: docker-compose up --build

The application will run on http://localhost:8080. Postgres (5432) and Redis (6379) are exposed for local testing.

Notes:
- Set real security.oauth2.jwk-set-uri and SECURITY_TELEMETRY_AGENT-TOKEN before production.
- Flyway migrations run automatically on startup (ensure DB credentials are correct).
