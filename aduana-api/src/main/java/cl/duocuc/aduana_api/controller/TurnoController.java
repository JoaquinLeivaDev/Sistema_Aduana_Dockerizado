package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.TurnoRequestDTO;
import cl.duocuc.aduana_api.dto.TurnoResponseDTO;
import cl.duocuc.aduana_api.service.TurnoService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/turnos")
@Tag(name = "Turnos", description = "Gestion de turnos de atencion, ventanillas y estados operativos")
public class TurnoController {

    private static final Logger log = LoggerFactory.getLogger(TurnoController.class);

    private final TurnoService service;

    public TurnoController(TurnoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar turnos", description = "Obtiene todos los turnos de atencion registrados.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turnos obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<TurnoResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/turnos - Listando turnos");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Turnos obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar turno por id", description = "Recupera un turno usando su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turno encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del turno", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/v1/turnos/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Turno encontrado"));
    }

    @GetMapping("/pasajero/{idPasajero}")
    @Operation(summary = "Buscar turno por pasajero", description = "Obtiene el turno asignado a un pasajero especifico.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turno del pasajero encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pasajero o turno no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> buscarPorPasajero(
            @Parameter(description = "Identificador del pasajero asociado al turno", example = "5")
            @PathVariable Long idPasajero) {
        log.info("GET /api/v1/turnos/pasajero/{}", idPasajero);
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarPorPasajero(idPasajero), "Turno del pasajero encontrado"));
    }

    @PostMapping
    @Operation(summary = "Registrar turno", description = "Crea un nuevo turno de atencion para un pasajero.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Turno registrado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del turno invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un turno de atencion",
                    required = true)
            @RequestBody @Valid TurnoRequestDTO dto) {
        log.info("POST /api/v1/turnos - Registrando turno para pasajero: {}", dto.getIdPasajero());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrarTurno(dto), "Turno registrado"));
    }

    @PatchMapping("/{id}/ventanilla/{numero}")
    @Operation(summary = "Asignar ventanilla", description = "Asigna un numero de ventanilla a un turno existente.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ventanilla asignada exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> asignarVentanilla(
            @Parameter(description = "Identificador del turno a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Numero de ventanilla que se asignara al turno", example = "4")
            @PathVariable Integer numero) {
        log.info("PATCH /api/v1/turnos/{}/ventanilla/{}", id, numero);
        return ResponseEntity.ok(ApiResponse.ok(
                service.asignarVentanilla(id, numero), "Ventanilla asignada exitosamente"));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del turno", description = "Modifica el estado operativo de un turno.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Estado invalido"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> actualizarEstado(
            @Parameter(description = "Identificador del turno a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado del turno", example = "ATENDIDO")
            @RequestParam String estado) {
        log.info("PATCH /api/v1/turnos/{}/estado - nuevo estado: {}", id, estado);
        return ResponseEntity.ok(ApiResponse.ok(
                service.actualizarEstado(id, estado), "Estado actualizado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar turno", description = "Elimina un turno registrado por su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Turno eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador del turno a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/v1/turnos/{}", id);
        service.eliminarTurno(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Turno eliminado"));
    }
}
