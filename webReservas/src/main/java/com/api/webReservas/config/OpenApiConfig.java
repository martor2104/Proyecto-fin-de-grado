package com.api.webReservas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;


@OpenAPIDefinition(
        info = @Info(
                description = "Documentation of a reservation website",
                title = "Web bar especification",
                version = "0.1",
                license = @License(
                        name = "Roberto",
                        url = "https://github.com/martor2104"
                )
        ),
        servers = @Server(
                description = "Local api",
                url = "http://localhost:8080"
        )
)
@SecurityScheme(
        name = "Authorized",
        description = "Authorizations of type admin",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
