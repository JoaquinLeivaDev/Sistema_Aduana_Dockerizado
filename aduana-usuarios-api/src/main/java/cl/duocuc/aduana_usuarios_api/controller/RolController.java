package cl.duocuc.aduana_usuarios_api.controller;

import cl.duocuc.aduana_usuarios_api.dto.ApiResponse;
import cl.duocuc.aduana_usuarios_api.dto.RolRequestDTO;
import cl.duocuc.aduana_usuarios_api.dto.RolResponseDTO;
import cl.duocuc.aduana_usuarios_api.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Gestion de roles de usuario del microservicio de usuarios")
public class RolController {

    private final RolService rolService;

    @GetMapping
    @Operation(summary = "Listar roles", description = "Obtiene todos los roles disponibles para los usuarios.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<RolResponseDTO>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok(rolService.obtenerTodos(), "Roles obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar rol por id", description = "Recupera un rol mediante su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Rol encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<RolResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del rol", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(rolService.buscarPorId(id), "Rol encontrado"));
    }

    @PostMapping
    @Operation(summary = "Crear rol", description = "Registra un nuevo rol de usuario.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Rol creado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del rol invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<RolResponseDTO>> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un nuevo rol",
                    required = true)
            @RequestBody @Valid RolRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(rolService.crear(dto), "Rol creado"));
    }
}
