# jee-msa-webflux-kafka-consumer
This project is a microservice developed in Java with Spring Boot and a hexagonal architecture. The objective is to consume messages from a Kafka topic and persist them in an H2 database.

## Technologies Used
* Java 17: Java version used for development.
* Spring Boot 3.5.3: Framework for creating Java applications.
* Gradle: Build and dependency management tool.
* Apache Kafka: Open-source distributed event streaming platform.
* H2 Database: In-memory database for data persistence.
* Spring WebFlux: Reactive web stack from Spring Framework.
* Spring Actuator: Production-ready features for monitoring and management.

## Prerequisites
Plugins that must be installed in your IDE:
* [Lombok](http://projectlombok.org/)
* [google-java-format](https://github.com/google/google-java-format)

## Project Structure
The project follows the hexagonal architecture structure, dividing the code into layers:
~~~
* src
  * main
    * java
      * com.villacis.<paradigm> (traditional = web, reactive = webflux)
        * application
          * input.port
          * output.port
          * service
          * service.mapper
        * domain
          * enums
          * exception
          * model
        * infrastructure
          * exception
          * input.adapter.kafka
          * input.adapter.kafka.config
          * input.adapter.kafka.consumer
          * input.adapter.rest
          * input.adapter.rest.config
          * input.adapter.rest.controller
          * input.adapter.rest.impl
          * input.adapter.rest.mapper
          * output.adapter
          * output.adapter.config
          * output.repository
          * output.repository.entity
          * output.repository.mapper
        * shared
          * impl
        - MainApplication.java
    * resources
      * static
      * templates
      - application.yml
      - banner.txt
  * test
- .gitignore
- build.gradle
- gradlew
- gradlew.bat
- README.md
- LICENSE
- settings.gradle
~~~

## Configuration
The application requires the following Kafka configuration in `application.yml`:
```yaml
kafka:
  bootstrap-servers: localhost:9092
  consumer:
    auto-offset-reset: earliest
    key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
  allowed-headers:
    - Content-Type
    - x-guid
    - x-session
  timezone: America/Guayaquil
  topic:
    name: develop-topic-log
```

## Message Structure
The Kafka topic expects messages with the following structure:

### Headers
* x-guid: UUID v4
* x-session: string

### Message Body
```json
{
  "data": {
    "fullName": "Jeferson Villacis",
    "email": "email@email.com"
  }
}
```

## Database
The application uses H2 database to persist messages with the following structure:
* Table: MSG_MENSAJE
* Fields:
  * ID: Auto-generated identifier
  * FECHA_REGISTRO: Timestamp of message reception (UTC-5 Ecuador/Guayaquil)
  * CABECERAS: JSON string containing message headers
  * MENSAJE: JSON string containing the message body

## Monitoring
The application includes Spring Actuator endpoints for monitoring:
* Health check: `/actuator/health`
* Metrics: `/actuator/metrics`
* Info: `/actuator/info`

## Build and Run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Contact
### If you have any questions or suggestions, please feel free to contact me:
* Name: Jeferson Villacís
* GitHub: https://github.com/jefersonvillacis
* LinkedIn: https://www.linkedin.com/in/jefersonvillacis

## Thanks for checking out this project! 🚀