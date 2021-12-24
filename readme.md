## Exchange Rate Service

## Reference Documentation
For building and running the application you need:
- [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Maven 3](https://maven.apache.org)
- [Spring Boot](https://start.spring.io)
- [Docker](https://www.docker.com/products/docker-desktop)

## Architecture
- The entire Application is developed in a layered approach having Controller, Service and Repository as the main components
- **Controller** layer consisting of the Api endpoints exposed to the outer world to access the API
- **Service** layer consisting of the Business Logic for the Checkout Application
- **Repository** layer consisting of In memory H2 database to store and provide access to data for the Service layer
- Flow of request goes as **Controller -> Service -> Repository**

## API Endpoints
- Exposed these Api Endpoints for the demonstration of Exchange Rate Application operations
    1. **GET /exchange/{currency}:** Retrieve the ECB reference rate of currencies against Euro. E.g. INR/EUR, USD/EUR
    2. **GET /exchange/{currency1}/{currency2}:** Retrieve the exchange rate of any 2 currencies. E.g. USD/INR, INR/USD, huf/usd, Usd/JpY
    3. **GET /exchange:** Retrieve the List of all currencies with their corresponding exchange rates
    4. **GET /exchange/{currency1}/{currency2}/{amount}:** Convert from Currency1 to Currency2 any given amount and retrieve the value
    5. **GET /link/{currency1}/{currency2}:** Retrieve a link for provided currency pair 
  
- Additional endpoint have been exposed to **Create** all currencies in the Database

## Flow
- The application's main functionality is to implement an Exchange Rate Service
- The System starts with no data in the **In-memory H2 Database**
- New Currencies can be injected into the DB by **POST** using the Json Provided for the Request Body for the add new currencies Api endpoint
- _Currencies.json_ has been added in the resources/data folder to load currencies
- The API endpoints for retrieving data can be accessed using the **GET** request with corresponding path variables for endpoints
- When the System is exited, the In-memory DB dies along with it destroying all the data

## Data Model
- The data modelling of this application has been done using a Relational for storage, access and retrieval of products and checkout details.
- The Forex Table in the DB contains all currency details with **id**, **name**, **rate** and **requestCount**
- The CurrencyPair model is being used to form the Currency Pair **name** and corresponding **rate** of different combinations of currencies

## Assumptions
- Speed and Concurrency is not the focus for this specific version of the Api
- We do not need to create multiple services and use Queues
- Caching has not been implemented to improve performance of Read requests

## Error and Exception Handling
- Certain Exception Handlers have been created to tackle some Error scenarios and throw corresponding exceptions
- Few examples of error scenarios
    1. User tries to access invalid url address
    3. User enters invalid parameters for **Currency** for retrieving currency pair rates
    4. User enters invalid parameter for **amount** while converting from one currency to another
    5. User enters 2 same currency for retrieving the url of a currency pair from a public website (https://www.investing.com)

## Testing Strategy, Code Quality and Test Coverage
- **Postman** was used as a client to test the API endpoints and validate the expected Status Codes and JSON response
- **Unit tests** have been added to check functionalities and correctness of the API, the API endpoints have been tested as well
- **Error Handling** has been performed to cover some errors and exception scenarios
- **Sonar** has been used to check for Bugs, Code Smells, Errors and Warnings to maintain code quality

## Running the application locally
There are several ways to run the Checkout application on your local machine.
One way is to package the application into a Jar/War with an IDE or by using **mvn package** command.
Alternatively the application can also be packaged into containers using Docker.

## Build and Deployment using Docker
Dockerfile has been configured to build and run the application on a container.
Multi Stage Dockerfile can be configured for Testing in future development
Commands to build and run the Application
- Build: **docker build -t app .**
- Run:  **docker run -it -p8080:8080 app**

## Access application
Application can be accessed using url http://localhost:8080/api/v1/exchange

## Improvements
- Additional Error Scenarios can be handled to cover more edge cases
- Add Swagger Open API for documentation of the API
- HATEOAS can be implemented to facilitate Hypermedia-driven Restful API's
- Multi Stage Configuration for Build, test and deployment in Docker for future development
- Code coverage can be increased through integration and end-to-end testing for future development