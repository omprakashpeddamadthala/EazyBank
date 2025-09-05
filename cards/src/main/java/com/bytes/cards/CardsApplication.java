package com.bytes.cards;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
        info = @Info(
                title = "Cards Microservice REST API Documentations",
                version = "v1.0",
                description = "EazyBank Cards Microservice REST API Documentations",
                contact = @Contact(
                        name = "Om Prakash",
                        email = "omprakashornold@gmail.com",
                        url = "https://omprakashornold.github.io/"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"

                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "EazyBank Cards microservice REST API Documentation",
                url = "https://www.eazybank.com/swagger-ui.html"
        )
)
public class CardsApplication {

    public static void main(String[] args) {
        SpringApplication.run( CardsApplication.class, args );
    }

}
