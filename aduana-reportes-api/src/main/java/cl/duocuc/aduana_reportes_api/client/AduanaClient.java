package cl.duocuc.aduana_reportes_api.client;

import cl.duocuc.aduana_reportes_api.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "aduana-api")
public interface AduanaClient {

    @GetMapping("/api/v1/pasajeros")
    ApiResponse<List<PasajeroResponse>> obtenerPasajeros();

    @GetMapping("/api/v1/pasajeros/{id}")
    ApiResponse<PasajeroResponse> obtenerPasajeroPorId(@PathVariable Long id);

    @GetMapping("/api/v1/vehiculos")
    ApiResponse<List<VehiculoResponse>> obtenerVehiculos();

    @GetMapping("/api/v1/usuarios/{id}")
    ApiResponse<UsuarioResponse> obtenerUsuarioPorId(@PathVariable Long id);

    @GetMapping("/api/v1/reportes")
    ApiResponse<List<ReporteResponseDTO>> obtenerReportes();

    @GetMapping("/api/v1/reportes/{id}")
    ApiResponse<ReporteResponseDTO> obtenerReportePorId(@PathVariable Long id);

    @GetMapping("/api/v1/reportes/usuario/{idUsuario}")
    ApiResponse<List<ReporteResponseDTO>> obtenerReportesPorUsuario(@PathVariable Long idUsuario);

    @GetMapping("/api/v1/reportes/tipo/{tipo}")
    ApiResponse<List<ReporteResponseDTO>> obtenerReportesPorTipo(@PathVariable String tipo);

    @PostMapping("/api/v1/reportes")
    ApiResponse<ReporteResponseDTO> registrarReporte(@RequestBody ReporteRequestDTO dto);

    @DeleteMapping("/api/v1/reportes/{id}")
    ApiResponse<Void> eliminarReporte(@PathVariable Long id);
}