# Distributed System

This project is a robust, high-performance distributed system using a hybrid **gRPC** and **REST** microservices, achieving asynchronous processing with **Kafka** message queuing, inspired by [Eng: Ahmed El Taweel](https://youtu.be/Ur6b1NWGbYE?si=F-2yqL7QPDIwyR6Z)

---

## Architecture

<img width="1302" height="733" alt="image" src="https://github.com/user-attachments/assets/a52e8adb-0a8c-44b5-8a3f-ad7835073bbd" />


The application consists of two main services (Spring Boot):

***gRPC Service***: A gRPC service that takes two numbers and immediately sums them then forwards results to Kafka.

***Rest Service***: A consumer REST service that reads messages from Kafka, adds the new value to an existing total, saves the updated value to an external file.

Communication Flow : 

1. Client makes gRPC request to the First Service with two numbers

2. gRPC Service adds numbers and sends the result to Kafka

3. REST Service consumes messages and add the current summation to the latest in the file

---

## Features

1- The gRPC Service ensures reliable message production through multiple layers of idempotency and fault tolerance:

**Business-Level Idempotency**: Each request carries a unique **UUID** requestId to guarantee that duplicate client calls (e.g., retries or network issues) do not produce duplicate results or Kafka messages.

**Kafka Producer Idempotency**: The producer is configured with **enable.idempotence=true**, ensuring that even in case of retries, Kafka prevents duplicate message delivery at the broker level.

**Retry Mechanism**: Safe automatic retries are enabled to handle transient network or broker failures, ensuring message delivery without loss.

**In-Memory Cache**: An in-memory cache temporarily stores recent processed requests, allowing the service to quickly identify duplicates and maintain consistency even under high load.


2- Load Balancing: **NGINX** is used as a load balancer to efficiently distribute incoming traffic across microservices, improving performance, scalability, and reliability.


3- Asynchronous Decoupling: **Apache Kafka** is used as the central message broker, enabling asynchronous, reliable, and decoupled communication between the gRPC producer service and the REST consumer service.

4- Observability: **Prometheus** and **Grafana** are integrated to provide complete observability for the system. Prometheus collects runtime metrics from Spring Boot services, and Grafana visualizes these metrics for real-time performance and health monitoring.

5- Load Testing: **K6** used to evaluate the performance, scalability, and stability under high concurrency. It simulates multiple clients sending requests simultaneously to ensure the system can handle real-world traffic efficiently.
