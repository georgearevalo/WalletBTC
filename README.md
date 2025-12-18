# Wallet-BTC
This project is a Spring Boot application that provides microservice to manage functionality for a Wallet BTC.

## Prerequisites

Before getting started, ensure you have the following prerequisites installed:

* Java 17 or later
* Maven 3.9 or later
* Git (optional, for cloning the repository)

## Installation Steps

1.  **Clone the Repository (Optional):**

    If you're using Git, clone the repository to your local machine:

    ```bash
    git clone [repository URL]
    cd [project name]
    ```

2.  **Build the Project:**

    Use Maven to build the project:

    ```bash
    mvn clean install
    ```

3.  **Run the Application:**

    * **From the JAR:**

        Navigate to the `target` directory and run the JAR file:

        ```bash
        java -jar [project name].jar

## Configuration

The application is configured through the `application.properties` file. Here are some important properties:

* `server.port`: Port on which the application runs.
* `segurity.config.username`: Username for access to the application.
* `segurity.config.password`: Password for access to the application.

You can modify these properties according to your needs.

## Resources
Projects consist of the following main API:

- [wallet-BTC-service](http://localhost:8080/wallet-BTC-services/v1/swagger-ui/index.html)

The application exposes the following endpoints:

* `GET /wallet`: Retrieves wallet.

## Dependencies

The project uses the following main dependencies:

* Spring Boot
* Spring Security
* Feign

## Contributing

If you'd like to contribute to this project, please follow these steps:

1.  Fork the repository.
2.  Create a feature branch (`git checkout -b feature/NewFeature`).
3.  Make your changes.
4.  Commit your changes (`git commit -am 'Add new feature'`).
5.  Push to the branch (`git push origin feature/NewFeature`).
6.  Open a pull request.