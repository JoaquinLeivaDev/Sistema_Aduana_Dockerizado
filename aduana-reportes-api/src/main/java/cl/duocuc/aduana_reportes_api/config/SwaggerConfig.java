package cl.duocuc.aduana_reportes_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Reportes - Sistema Aduana")
                        .version("1.0")
                        .description("Documentación de endpoints para el microservicio de Reportes"));
    }
}
