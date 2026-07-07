package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.VehiculoRequestDTO;
import cl.duocuc.aduana_api.dto.VehiculoResponseDTO;
import cl.duocuc.aduana_api.service.VehiculoService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
@Tag(name = "Vehiculos", description = "Gestion de vehiculos declarados y sus operaciones aduaneras")
public class VehiculoController {

    private static final Logger log = LoggerFactory.getLogger(VehiculoController.class);

    private final VehiculoService service;

    public VehiculoController(VehiculoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar vehiculos", description = "Obtiene todos los vehiculos registrados en el sistema.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculos obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<VehiculoResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/vehiculos - Listando todos los vehiculos");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Vehiculos obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vehiculo por id", description = "Recupera un vehiculo por su identificador interno.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculo encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del vehiculo", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/v1/vehiculos/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Vehiculo encontrado"));
    }

    @GetMapping("/patente/{patente}")
    @Operation(summary = "Buscar vehiculo por patente", description = "Recupera un vehiculo usando su patente registrada.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculo encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> buscarPorPatente(
            @Parameter(description = "Patente del vehiculo a consultar", example = "ABCD12")
            @PathVariable String patente) {
        log.info("GET /api/v1/vehiculos/patente/{}", patente);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorPatente(patente), "Vehiculo encontrado"));
    }

    @PostMapping
    @Operation(summary = "Registrar vehiculo", description = "Crea un nuevo vehiculo declarado en la aduana.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Vehiculo registrado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del vehiculo invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pasajero asociado no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar un vehiculo",
                    required = true)
            @RequestBody @Valid VehiculoRequestDTO dto) {
        log.info("POST /api/v1/vehiculos - Registrando vehiculo patente: {}", dto.getPatente());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrarVehiculo(dto), "Vehiculo registrado"));
    }

    @PatchMapping("/{id}/satva")
    @Operation(summary = "Generar SATVA", description = "Genera la documentacion SATVA asociada a un vehiculo.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "SATVA generado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> generarSATVA(
            @Parameter(description = "Identificador del vehiculo para generar SATVA", example = "1")
            @PathVariable Long id) {
        log.info("PATCH /api/v1/vehiculos/{}/satva - Generando SATVA", id);
        return ResponseEntity.ok(ApiResponse.ok(service.generarSATVA(id), "SATVA generado exitosamente"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehiculo", description = "Actualiza la informacion de un vehiculo existente.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculo actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del vehiculo invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> actualizar(
            @Parameter(description = "Identificador del vehiculo a actualizar", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del vehiculo",
                    required = true)
            @RequestBody @Valid VehiculoRequestDTO dto) {
        log.info("PUT /api/v1/vehiculos/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.actualizarVehiculo(id, dto), "Vehiculo actualizado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vehiculo", description = "Elimina un vehiculo registrado por su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vehiculo eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vehiculo no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador del vehiculo a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/v1/vehiculos/{}", id);
        service.eliminarVehiculo(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Vehiculo eliminado"));
    }
}
