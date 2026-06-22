package cl.duocuc.aduana_usuarios_api.client;

import cl.duocuc.aduana_usuarios_api.dto.ApiResponse;
import cl.duocuc.aduana_usuarios_api.dto.ReporteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

/**
 * Cliente hacia el microservicio aduana-reportes-api.
 * En perfil "local" Eureka resuelve el nombre lógico (name); en perfil "prod"
 * se usa la url fija configurada en application.yml (ver propiedad reportes.api.url).
 */
@FeignClient(name = "aduana-reportes-api")
public interface ReportesClient {

    @GetMapping("/api/v1/reportes/usuario/{idUsuario}")
    ApiResponse<List<ReporteResponseDTO>> obtenerReportesPorUsuario(@PathVariable Long idUsuario);
}
