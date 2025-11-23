# Distributed System

This project is a robust, high-performance Microservices Distributed System using a hybrid **gRPC** and **REST** microservices, achieving asynchronous processing with **Kafka** message queuing, inspired by [Eng: Ahmed El Taweel](https://youtu.be/Ur6b1NWGbYE?si=F-2yqL7QPDIwyR6Z)

---

## Architecture
<img width="1266" height="651" alt="Architecture" src="https://github.com/user-attachments/assets/5cd7f4b2-52e2-4154-a752-ee75488e609d" />

---

The application consists of two main services (Spring Boot):

***gRPC Service***: A gRPC service that takes two numbers and immediately sums them then forwards results to Kafka.

***REST Service***: A consumer REST service that reads messages from Kafka, adds the new value to an existing total, saves the updated value to an external file.

Communication Flow : 

1. Client makes gRPC request to the First Service with two numbers
2. gRPC Service adds numbers and sends the result to Kafka
3. REST Service consumes messages and add the current summation to the latest in the file

---

# Features
## 1. gRPC Service

The system provides a **gRPC endpoint** that performs simple computation, summing two numbers. Beyond basic computation, the service is designed with strong **reliability and idempotency guarantees** to ensure safe operation in distributed environments.

### Key Features

- **Business-Level Idempotency**:  
  Each request includes a unique **UUID `requestId`**, which guarantees that duplicate client calls—whether due to retries, network issues, or user error—do not result in duplicate computation or duplicated Kafka messages.
- **Kafka Producer Idempotency**:  
  The Kafka producer is configured with **`enable.idempotence=true`**, ensuring that even if retries occur, the broker will prevent duplicate message delivery. This provides exactly-once delivery semantics for asynchronous messaging.
- **Retry Mechanism**:  
  Safe automatic retries are enabled to handle transient failures, such as temporary network glitches or Kafka broker unavailability, ensuring messages are reliably delivered without loss.
- **In-Memory Cache**:  
  A temporary in-memory cache stores recent processed requests, allowing the service to quickly detect duplicates and maintain consistency, even under high load or repeated client calls.

This combination of **idempotency, retries, and caching** ensures that the gRPC service remains **resilient, consistent, and reliable**, even in highly distributed and high-traffic environments.

## 2. REST Service

- Provides REST endpoints for saving summation results to external file for auditing or offline processing.

## 3. Load Testing with k6

The system includes robust load testing using **k6**, a modern load-testing tool designed to simulate realistic traffic patterns and measure system performance. This feature ensures the application can handle high loads and maintain reliability under stress.

### Key Features

- **Ramping Virtual Users**: Gradually increases the number of simulated users to observe system behavior under rising load, helping identify potential bottlenecks.  
- **Randomized Endpoint Testing**: Simulates requests to different API endpoints randomly, ensuring all services are tested under realistic conditions.  
- **Performance Thresholds**: Defines acceptable limits for error rates and response times, so the system is automatically flagged if performance degrades.  
- **Realistic User Behavior**: Introduces random delays between requests to mimic real-world user interactions, rather than sending constant traffic.  
- **Peak Load Simulation**: Can scale to thousands of concurrent users to test the system’s ability to handle extreme traffic spikes.  
- **Comprehensive Metrics**: Measures throughput, latency, error rates, and other performance indicators, providing detailed insights for optimization.  

Overall, k6 load testing helps ensure that services remain performant, resilient, and reliable before deployment to production.

## 4. Observability

The system uses a full observability stack to monitor performance, errors, and traces:

- **Grafana**: Dashboard for visualizing metrics, logs, and traces.
- **Prometheus**: Collects system and application metrics such as CPU usage, JVM heap, queue latency, error rate (5xx), and RPS.
- **Loki**: Centralized logging for debugging and tracking application events.
- **Tempo**: Distributed tracing for analyzing request flows and identifying bottlenecks.

**Metrics Captured:**

| Metric                  | Description                                |
|-------------------------|--------------------------------------------|
| CPU Usage               | Tracks system CPU utilization              |
| JVM Heap                | Monitors Java memory consumption           |
| Error Rate (5xx)        | Tracks server-side errors                  |
| Requests per Second     | Measures throughput                        |
| Queue Latency           | Measures message queue delays              |

---
## Screenshots
### Loki Logs Viewer
<img width="1893" height="920" alt="Screenshot 2025-11-23 160843" src="https://github.com/user-attachments/assets/dd40a7f2-abeb-4735-a1e9-7654c6a9ba6c" />

### Tempo Distributed Traces
<img width="1891" height="930" alt="file_2025-11-22_19 54 40" src="https://github.com/user-attachments/assets/894617f9-182b-47a1-98da-5685cdf6a127" />

### Grafana Dashboard
<img width="1896" height="918" alt="file_2025-11-22_18 54 25" src="https://github.com/user-attachments/assets/e0db15f1-9d2e-4b92-b268-edcfffee818d" />

### Kafka Messege
<img width="1877" height="917" alt="file_2025-11-23_13 43 39" src="https://github.com/user-attachments/assets/8a73f996-3e95-4ffc-bd20-efe2fd4b1b8f" />

### Docker Container
<img width="1914" height="1035" alt="file_2025-11-23_13 42 11" src="https://github.com/user-attachments/assets/fe0d902d-5949-4c38-bb05-ecc11289a510" />

---

## Prerequisites

Make sure you have installed:

- Java 24
- Maven
- Docker & Docker Compose
- Kafka & ZooKeeper
- Node.js (for k6, optional if using CLI)
  
---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/your-repo.git
cd your-repo
```

### 2. Start Spring Boot App

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### 3. Start Zookeaper, Kafka
```bash
# Start ZooKeeper
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
# Start Kafka broker
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

### 4. Start Observability Stack
```bash
docker-compose up -d
```

### 5. Run Load Testing with k6
```bash
k6 run load_test.js
```
