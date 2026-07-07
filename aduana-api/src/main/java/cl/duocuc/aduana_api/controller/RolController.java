package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.RolRequestDTO;
import cl.duocuc.aduana_api.dto.RolResponseDTO;
import cl.duocuc.aduana_api.service.RolService;
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
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Gestion de roles y perfiles disponibles en el sistema")
public class RolController {

    private static final Logger log = LoggerFactory.getLogger(RolController.class);

    private final RolService service;

    public RolController(RolService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar roles", description = "Obtiene todos los roles disponibles para los usuarios del sistema.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<RolResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/roles - Listando roles");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Roles obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar rol por id", description = "Recupera un rol usando su identificador interno.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rol encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<RolResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del rol", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/v1/roles/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Rol encontrado"));
    }

    @PostMapping
    @Operation(summary = "Registrar rol", description = "Crea un nuevo rol para asignacion de permisos o perfiles.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Rol registrado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del rol invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<RolResponseDTO>> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar un nuevo rol",
                    required = true)
            @RequestBody @Valid RolRequestDTO dto) {
        log.info("POST /api/v1/roles - Registrando rol: {}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrarRol(dto), "Rol registrado"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol", description = "Actualiza la informacion de un rol existente.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del rol invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<RolResponseDTO>> actualizar(
            @Parameter(description = "Identificador del rol a actualizar", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del rol",
                    required = true)
            @RequestBody @Valid RolRequestDTO dto) {
        log.info("PUT /api/v1/roles/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.actualizarRol(id, dto), "Rol actualizado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol", description = "Elimina un rol registrado por su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador del rol a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/v1/roles/{}", id);
        service.eliminarRol(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Rol eliminado"));
    }
}
