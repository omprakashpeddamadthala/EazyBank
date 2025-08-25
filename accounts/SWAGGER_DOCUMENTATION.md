# Swagger Documentation for Accounts Microservice

## Swagger Dependencies
The project already includes the necessary Swagger dependencies in the `pom.xml` file:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-api</artifactId>
    <version>2.8.11</version>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.11</version>
</dependency>
```

## Swagger Configuration
The Swagger configuration is defined in the `application.yml` file:

```yaml
# Swagger/OpenAPI Configuration
springdoc:
  swagger-ui:
    path: /swagger-ui/index.html
  api-docs:
    path: /v3/api-docs
```

## Accessing Swagger UI
To access the Swagger UI for this microservice:

1. Start the application
2. Open a web browser
3. Navigate to: `http://localhost:8080/swagger-ui/index.html`

This will display the Swagger UI interface where you can:
- View all available REST endpoints
- Test the API endpoints directly from the browser
- View request/response models and schemas
- Download the OpenAPI specification

## Accessing OpenAPI Documentation
The raw OpenAPI documentation in JSON format is available at:
`http://localhost:8080/v3/api-docs`

## Additional Information
- The Swagger UI automatically generates documentation based on your Spring controllers and models
- No additional annotations are required in the controller classes, though you can add annotations like `@Tag`, `@Operation`, etc. to enhance the documentation
- The port (8080) may vary depending on your application configuration
