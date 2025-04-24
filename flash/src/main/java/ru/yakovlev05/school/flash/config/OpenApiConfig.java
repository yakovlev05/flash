package ru.yakovlev05.school.flash.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Flash",
                description = "API мессенджера Flash"
        ),
        servers = @Server(url = "/")
)
// Теперь в cookie
//@SecurityScheme(
//        name = "JWT",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
public class OpenApiConfig {
}
