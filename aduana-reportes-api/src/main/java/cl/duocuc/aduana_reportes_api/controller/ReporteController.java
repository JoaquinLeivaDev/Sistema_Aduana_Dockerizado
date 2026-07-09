package cl.duocuc.aduana_reportes_api.controller;

import cl.duocuc.aduana_reportes_api.dto.ApiResponse;
import cl.duocuc.aduana_reportes_api.dto.PasajeroResponse;
import cl.duocuc.aduana_reportes_api.dto.ReporteRequestDTO;
import cl.duocuc.aduana_reportes_api.dto.ReporteResponseDTO;
import cl.duocuc.aduana_reportes_api.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes", description = "Gestion y consulta de reportes del microservicio de reportes")
public class ReporteController {

    private final ReporteService service;

    @GetMapping
    @Operation(summary = "Listar reportes", description = "Obtiene todos los reportes registrados en el microservicio.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reportes obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> listarTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reporte por id", description = "Recupera un reporte mediante su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reporte encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<ReporteResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del reporte", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Buscar reportes por usuario", description = "Lista los reportes asociados a un usuario especifico.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reportes del usuario obtenidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> buscarPorUsuario(
            @Parameter(description = "Identificador del usuario asociado a los reportes", example = "1")
            @PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.obtenerPorUsuario(idUsuario));
    }

    @GetMapping("/consolidado/pasajeros")
    @Operation(summary = "Generar consolidado de pasajeros", description = "Obtiene el reporte consolidado de pasajeros generado por el microservicio.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reporte consolidado obtenido exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<PasajeroResponse>>> reportePasajeros() {
        return ResponseEntity.ok(service.generarReportePasajeros());
    }

    @PostMapping
    @Operation(summary = "Registrar reporte", description = "Crea un nuevo reporte en el microservicio de reportes.")
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
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrarReporte(dto));
    }

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
        return ResponseEntity.ok(service.eliminarReporte(id));
    }
}
