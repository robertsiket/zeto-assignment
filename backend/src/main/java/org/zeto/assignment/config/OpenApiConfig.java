package org.zeto.assignment.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Configuration class for OpenAPI documentation.
 * <p>
 * This class defines metadata and configuration for the OpenAPI specification
 * of the Zeto EDF File Processing API. It includes details about the API such
 * as title, version, description, contact information, license, and server details.
 * <p>
 * The API is designed for processing and retrieving metadata of EDF (European Data Format) files.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Zeto EDF File Processing API",
                version = "v1",
                description = "API for processing and retrieving EDF (European Data Format) file metadata.",
                contact = @Contact(name = "Zeto Assignment", email = "support@example.com"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Server")
        }
)
public class OpenApiConfig {
}
