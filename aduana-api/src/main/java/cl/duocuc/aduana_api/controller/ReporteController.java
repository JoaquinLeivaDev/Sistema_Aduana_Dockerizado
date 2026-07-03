package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.ReporteRequestDTO;
import cl.duocuc.aduana_api.dto.ReporteResponseDTO;
import cl.duocuc.aduana_api.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Reportes", description = "Endpoints para la gestión de reportes")
@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private static final Logger log = LoggerFactory.getLogger(ReporteController.class);

    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los reportes")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/reportes");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Reportes obtenidos"));
    }

    @Operation(summary = "Buscar reporte por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReporteResponseDTO>> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/reportes/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Reporte encontrado"));
    }

    @Operation(summary = "Buscar reportes por usuario")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> buscarPorUsuario(
            @PathVariable Long idUsuario) {
        log.info("GET /api/v1/reportes/usuario/{}", idUsuario);
        return ResponseEntity.ok(ApiResponse.ok(
                service.obtenerPorUsuario(idUsuario), "Reportes del usuario obtenidos"));
    }

    @Operation(summary = "Buscar reportes por tipo")
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> buscarPorTipo(
            @PathVariable String tipo) {
        log.info("GET /api/v1/reportes/tipo/{}", tipo);
        return ResponseEntity.ok(ApiResponse.ok(
                service.obtenerPorTipo(tipo), "Reportes por tipo obtenidos"));
    }

    @Operation(summary = "Registrar un nuevo reporte")
    @PostMapping
    public ResponseEntity<ApiResponse<ReporteResponseDTO>> registrar(
            @RequestBody @Valid ReporteRequestDTO dto) {
        log.info("POST /api/v1/reportes");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrar(dto), "Reporte registrado"));
    }

    @Operation(summary = "Eliminar reporte")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/reportes/{}", id);
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Reporte eliminado"));
    }
}