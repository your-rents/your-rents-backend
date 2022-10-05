# Your Rents back-end

[![Build Status](https://api.travis-ci.org/your-rents/your-rents-backend.svg?branch=develop)](https://travis-ci.org/your-rents/your-rents-backend)

The Java back-end services for the Your Rent system.

## Prerequisites

- jdk 11.0.4 or higher
- maven 3.6.1 or higher
- docker 1.6.0 or higher (<https://www.docker.com/>)
- docker-compose (<https://docs.docker.com/compose/install/>)

## To build
```shell
$ mvn clean install
```

## To start the app
```shell
$ cd your-rents-api
$ mvn spring-boot:run
```

## Swagger

<http://localhost:8080/swagger-ui/>

## Developer's Backend Environment

In the development environment you should use the provided Docker containers.

### Start the environment

From the `your-rents-backend` directory, type the following command:

```console
docker compose -f "docker-compose-services.yml" up -d --build
```

For stopping it, type the following command:

```console
docker compose -f "docker-compose-services.yml" down
```

### Initialize the database schema

Go to the `your-rents-data` directory, and type the following command:

```console
./mvnw flyway:migrate
```

### Inspect the PostgreSQL database

You can use your preferred tool for connecting to the database, using the following parameters:

```properties
host: localhost
port: 25432
database: your_rents
username: your_rents
password: your_rents
```

or you can use the provided [Adminer](https://www.adminer.org/) installation, pointing your browser at the following address:

<http://localhost:18080/?pgsql=postgres_your_rents&username=your_rents&db=your_rents>
