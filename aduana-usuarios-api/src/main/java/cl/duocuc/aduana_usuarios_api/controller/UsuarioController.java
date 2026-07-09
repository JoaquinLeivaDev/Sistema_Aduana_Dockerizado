package cl.duocuc.aduana_usuarios_api.controller;

import cl.duocuc.aduana_usuarios_api.dto.ApiResponse;
import cl.duocuc.aduana_usuarios_api.dto.LoginRequestDTO;
import cl.duocuc.aduana_usuarios_api.dto.ReporteResponseDTO;
import cl.duocuc.aduana_usuarios_api.dto.UsuarioRequestDTO;
import cl.duocuc.aduana_usuarios_api.dto.UsuarioResponseDTO;
import cl.duocuc.aduana_usuarios_api.service.UsuarioService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestion de usuarios, autenticacion y consulta de reportes externos")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<UsuarioResponseDTO>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.obtenerTodos(), "Usuarios obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por id", description = "Recupera un usuario mediante su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del usuario", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.buscarPorId(id), "Usuario encontrado"));
    }

    @PostMapping
    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario; la contrasena se almacena hasheada.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del usuario invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar un nuevo usuario",
                    required = true)
            @RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(usuarioService.registrarUsuario(dto), "Usuario registrado"));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Valida las credenciales de acceso del usuario.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Credenciales o datos de acceso invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales del usuario para iniciar sesion",
                    required = true)
            @RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.login(dto), "Login exitoso"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza username, password o rol de un usuario existente.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del usuario invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> actualizar(
            @Parameter(description = "Identificador del usuario a actualizar", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del usuario",
                    required = true)
            @RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.actualizarUsuario(id, dto), "Usuario actualizado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario registrado por su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador del usuario a eliminar", example = "1")
            @PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuario eliminado"));
    }

    @GetMapping("/{id}/reportes")
    @Operation(summary = "Obtener reportes de un usuario", description = "Consulta los reportes asociados a un usuario en el microservicio de reportes.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reportes del usuario obtenidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> obtenerReportes(
            @Parameter(description = "Identificador del usuario cuyos reportes se consultan", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.obtenerReportesPorUsuario(id), "Reportes del usuario obtenidos"));
    }
}
