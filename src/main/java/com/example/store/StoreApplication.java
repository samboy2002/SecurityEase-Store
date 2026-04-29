package com.example.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "store",
                        version = "1.1",
                        description = "A simple store",
                        contact =
                                @Contact(
                                        name = "SecuritEase Dev",
                                        url = "https://www.securitease.com",
                                        email = "internal@securitease.com"),
                        termsOfService = "https://www.securitease.com",
                        license =
                                @License(
                                        name = "Apache 2.0",
                                        url = "https://www.apache.org/licenses/LICENSE-2.0.html")),
        servers = @Server(url = "http://localhost:8080", description = "dev"))
@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }
}
