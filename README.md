# Release Manager



Each time a service is deployed with a new version, the system version is incremented and a snapshot of all deployed services is saved. Deploying the same version again does not change the system version.

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Data JPA / Hibernate
- H2 Database
- Liquibase
- Gradle
- JUnit 5 / Mockito

## Running the Application

Run the application using the Gradle wrapper:

```bash
./gradlew bootRun
```

On Windows:

```bash
gradlew.bat bootRun
```

The API runs at `http://localhost:8080`.

## API Endpoints

### Deploy a service

`POST /deploy`

```json
{
  "name": "Service A",
  "version": 1
}
```

Response:

```json
1
```

The response is the current system version.

### Get deployed services

`GET /services?systemVersion=1`

Response:

```json
[
  {
    "name": "Service A",
    "version": 1
  }
]
```

This returns the services deployed at the requested system version.

## Running Tests

```bash
./gradlew test
```


## Design Choices

- A global system version tracks changes across all services.
- Current deployments are stored separately from historical snapshots.
- Each system version keeps a complete snapshot of deployed services.
- Pessimistic locking is used to coordinate concurrent deployments.
- Liquibase manages the database schema.

## Notes

- H2 runs in memory, so data is reset when the application restarts.
- The implementation uses a single global deployment environment.
