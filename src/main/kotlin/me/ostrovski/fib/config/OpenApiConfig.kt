package me.ostrovski.fib.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import org.springframework.context.annotation.Configuration

/**
 * Configuration related to the OpenAPI documentation.
 */
@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "\${app.info.name}",
        description = "\${app.info.description}",
        version = "\${app.info.version}",
        license = License(name = "\${app.info.license}", url = "\${app.info.licenseUrl}")
    )
)
class OpenApiConfig
