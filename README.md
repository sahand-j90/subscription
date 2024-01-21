# Simple Subscription Using the Outbox Pattern
I have implemented the Outbox Pattern for achieving eventual consistency with clients in a simple subscription scenario.

## Tools and Patterns Used
### Outbox Pattern
I utilized a combination of Postgres as the RDBMS, Hibernate listener for creating domain events, an embedded Debezium engine as CDC, and Kafka as the message broker.
- Every domain event has a correlationId, domain, eventType and payload properties.
- Because of at least once nature of the CDC, I use a unique idempotent key.
- To trace each domain event, it also has spanId and traceId properties

### Integration Test
For testing the Outbox Pattern integration, I leveraged Test Containers.

### CI/CD
I integrated GitHub workflows for CI/CD. I have defined a simple branching strategy—develop and master. Every push into master triggers a workflow to run tests, build artifacts, create a Docker image, and push it to the Docker Hub registry. This ensures that when you run the application using the provided docker-compose, you have access to the latest version.

### Redis
I implemented Redis for distributed locking and rate limiting.

### Documentation using Swagger
The REST API documentation is generated using Swagger.

### Security
For security implementation, I used Spring Security and JWT tokens. I employed a combination of design patterns, such as factory, command, and template method, along with the chain of responsibility provided by the Spring Authentication Manager, to ensure open-closed functionality for adding new authentication flows.

### Schema Migration
Liquibase was used for schema migration.

### Virtual Thread
I use Java 21 Virtual Threads instead of the reactive alternative.

### Logging
I utilized Logstash and a file appender to produce JSON log files.

### Tracing
For tracing, I used Micrometer Brave.

### Monitoring
I employed Micrometer Prometheus to publish metrics.

### Actuator
Application Actuator is utilized.

### Application Profiles
Various profiles are used for enabling/disabling Swagger, as well as console and file logging.

### DTO Mapping
MapStruct is used for DTO mapping.

# Scenario
This is a simple subscription scenario for defining and managing user subscriptions
## Subscriber and Subscription
There is tow main Domain Entities: Subscriber and Subscription. A subscriber can have multiple subscriptions.
Each subscription has a start date (from) and an end date (to)

### Each subscription has one of the following states:
- FINISHED: Subscriptions that have expired
- ACTIVE: When a subscription is activated
- RESERVED: Subscriptions whose start time has not yet reached
The state of the subscription is processed at the time of its creation. A scheduler also monitors the status of expired subscriptions.

### There is three Domain Events:
- CREATED: When a subscription is created
- ACTIVATED: Only one subscription can be in active state
- EXPIRED: When a subscription ends

## User
There is also a user entity. A user can have ADMIN or NORMAL role. Only Admin users can define other users.


#Run Application
* **`docker compose -f docker-compose.yml up -d`**
* swagger: 
  - [http://localhost:9090/swagger-ui/index.html]()
  - username: Admin
  - password: Admin
* kafka-ui: [http//localhost:8085 ]()
* postgres-ui: [http//localhost:8085]() (uncomment in docker-compose)
