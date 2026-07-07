package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.PasajeroRequestDTO;
import cl.duocuc.aduana_api.dto.PasajeroResponseDTO;
import cl.duocuc.aduana_api.service.PasajeroService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pasajeros")
@Tag(name = "Pasajeros", description = "Gestion de pasajeros y su informacion de identificacion")
public class PasajeroController {

    private static final Logger log = LoggerFactory.getLogger(PasajeroController.class);

    private final PasajeroService service;

    public PasajeroController(PasajeroService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar pasajeros", description = "Obtiene todos los pasajeros registrados en el sistema.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pasajeros obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<PasajeroResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/pasajeros - Listando todos los pasajeros");
        List<PasajeroResponseDTO> pasajeros = service.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.ok(pasajeros, "Pasajeros obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pasajero por id", description = "Recupera un pasajero usando su identificador interno.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pasajero encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<PasajeroResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del pasajero", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/v1/pasajeros/{} - Buscando pasajero", id);
        PasajeroResponseDTO pasajero = service.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(pasajero, "Pasajero encontrado"));
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Buscar pasajero por RUT", description = "Recupera un pasajero mediante su RUT registrado.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pasajero encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<PasajeroResponseDTO>> buscarPorRut(
            @Parameter(description = "RUT del pasajero a consultar", example = "12345678-9")
            @PathVariable String rut) {
        log.info("GET /api/v1/pasajeros/rut/{} - Buscando pasajero por RUT", rut);
        PasajeroResponseDTO pasajero = service.buscarPorRut(rut);
        return ResponseEntity.ok(ApiResponse.ok(pasajero, "Pasajero encontrado"));
    }

    @PostMapping
    @Operation(summary = "Registrar pasajero", description = "Crea un nuevo pasajero con sus datos de identificacion.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Pasajero registrado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del pasajero invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<PasajeroResponseDTO>> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar un pasajero",
                    required = true)
            @RequestBody @Valid PasajeroRequestDTO dto) {
        log.info("POST /api/v1/pasajeros - Registrando pasajero con RUT: {}", dto.getRut());
        PasajeroResponseDTO creado = service.registrarPasajero(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(creado, "Pasajero registrado exitosamente"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pasajero", description = "Actualiza la informacion de un pasajero existente.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pasajero actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del pasajero invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<PasajeroResponseDTO>> actualizar(
            @Parameter(description = "Identificador del pasajero a actualizar", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del pasajero",
                    required = true)
            @RequestBody @Valid PasajeroRequestDTO dto) {
        log.info("PUT /api/v1/pasajeros/{} - Actualizando pasajero", id);
        PasajeroResponseDTO actualizado = service.actualizarPasajero(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(actualizado, "Pasajero actualizado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pasajero", description = "Elimina un pasajero registrado por su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pasajero eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador del pasajero a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/v1/pasajeros/{} - Eliminando pasajero", id);
        service.eliminarPasajero(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Pasajero eliminado"));
    }
}
