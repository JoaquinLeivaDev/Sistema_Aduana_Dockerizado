package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.ReporteRequestDTO;
import cl.duocuc.aduana_api.dto.ReporteResponseDTO;
import cl.duocuc.aduana_api.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Reportes", description = "Endpoints para la gestión de reportes")
@RestController
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes Aduana", description = "Gestion de reportes operativos generados desde el modulo principal")
public class ReporteController {

    private static final Logger log = LoggerFactory.getLogger(ReporteController.class);

    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los reportes")
    @GetMapping
    @Operation(summary = "Listar reportes", description = "Obtiene todos los reportes registrados en el sistema.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reportes obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/reportes");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Reportes obtenidos"));
    }

    @Operation(summary = "Buscar reporte por ID")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar reporte por id", description = "Recupera un reporte especifico usando su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reporte encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<ReporteResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del reporte", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/v1/reportes/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Reporte encontrado"));
    }

    @Operation(summary = "Buscar reportes por usuario")
    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Buscar reportes por usuario", description = "Lista los reportes creados o asociados a un usuario.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reportes del usuario obtenidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> buscarPorUsuario(
            @Parameter(description = "Identificador del usuario asociado a los reportes", example = "3")
            @PathVariable Long idUsuario) {
        log.info("GET /api/v1/reportes/usuario/{}", idUsuario);
        return ResponseEntity.ok(ApiResponse.ok(
                service.obtenerPorUsuario(idUsuario), "Reportes del usuario obtenidos"));
    }

    @Operation(summary = "Buscar reportes por tipo")
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar reportes por tipo", description = "Filtra los reportes segun el tipo indicado.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reportes por tipo obtenidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No se encontraron reportes para el tipo indicado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> buscarPorTipo(
            @Parameter(description = "Tipo de reporte a consultar", example = "INCIDENCIA")
            @PathVariable String tipo) {
        log.info("GET /api/v1/reportes/tipo/{}", tipo);
        return ResponseEntity.ok(ApiResponse.ok(
                service.obtenerPorTipo(tipo), "Reportes por tipo obtenidos"));
    }

    @Operation(summary = "Registrar un nuevo reporte")
    @PostMapping
    @Operation(summary = "Registrar reporte", description = "Crea un nuevo reporte operativo dentro del sistema.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Reporte registrado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del reporte invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario asociado no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<ReporteResponseDTO>> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar un reporte",
                    required = true)
            @RequestBody @Valid ReporteRequestDTO dto) {
        log.info("POST /api/v1/reportes");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrar(dto), "Reporte registrado"));
    }

    @Operation(summary = "Eliminar reporte")
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte registrado por su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reporte eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador del reporte a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/v1/reportes/{}", id);
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Reporte eliminado"));
    }
}
