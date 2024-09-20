# Project Overview
This repository focuses on Solace PubSub+ messaging technology, and it is part of the Fundamentals of Solace Development course. The commits indicate that several messaging patterns and techniques were implemented, including load balancing, request-reply patterns, and REST API integration.

# Key Features and Commits
* Implemented a basic publish-subscribe pattern using a single node.
* Load Balancing with a Queue: Load balancing feature added to distribute messages across consumers via queues.
* Multi-Protocol Support - Request Reply: Implemented the request-reply pattern using multi-protocol capabilities.
* REST Request: Built an application capable of sending a REST request.



## How to Run the Project Locally

### Prerequisites
- Maven
- JDK 21

Set up Solace: Set up a Solace PubSub+ instance and configure the broker details in `application.properties` or `application.yml.`

### Run the Spring Boot Application 
```shell
cd auth-server && mvn spring-boot:run
```

```shell
cd web-app-server && mvn spring-boot:run
```

Access the Application: Once the application is running, access it locally by navigating to: http://localhost:8081
