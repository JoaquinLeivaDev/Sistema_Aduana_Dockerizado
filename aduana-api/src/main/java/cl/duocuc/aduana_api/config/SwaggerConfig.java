package cl.duocuc.aduana_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aduana API")
                        .description("""
                                API REST del Sistema de Control Aduanero.
                                Gestiona pasajeros, vehículos, documentos, turnos, usuarios, roles y reportes.
                                Actúa como proyecto base para la arquitectura de microservicios.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Aduana DuocUC")
                                .email("contacto@aduana.cl")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor local"),
                        new Server()
                                .url("${ADUANA_API_URL}")
                                .description("Servidor remoto (Railway/Render)")
                ));
    }
}