package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.LoginRequestDTO;
import cl.duocuc.aduana_api.dto.ReporteResponseDTO;
import cl.duocuc.aduana_api.dto.UsuarioRequestDTO;
import cl.duocuc.aduana_api.dto.UsuarioResponseDTO;
import cl.duocuc.aduana_api.service.UsuarioService;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestion de usuarios internos, autenticacion y consulta de reportes")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados en el sistema.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<UsuarioResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/usuarios - Listando usuarios");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Usuarios obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por id", description = "Recupera un usuario usando su identificador interno.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del usuario", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/v1/usuarios/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Usuario encontrado"));
    }

    @GetMapping("/{id}/reportes")
    @Operation(summary = "Obtener reportes de usuario", description = "Lista los reportes asociados a un usuario especifico.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reportes del usuario obtenidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> obtenerReportes(
            @Parameter(description = "Identificador del usuario cuyos reportes se consultan", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/v1/usuarios/{}/reportes", id);
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerReportesPorUsuario(id), "Reportes del usuario obtenidos"));
    }

    @PostMapping
    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario interno con sus credenciales y rol asociado.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del usuario invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar un usuario",
                    required = true)
            @RequestBody @Valid UsuarioRequestDTO dto) {
        log.info("POST /api/v1/usuarios - Registrando usuario: {}", dto.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrarUsuario(dto), "Usuario registrado"));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Valida las credenciales de acceso de un usuario.")
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
        log.info("POST /api/v1/usuarios/login - Login: {}", dto.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(service.login(dto), "Login exitoso"));
    }

    @PostMapping("/{id}/logout")
    @Operation(summary = "Cerrar sesion de usuario", description = "Registra el cierre de sesion para un usuario identificado.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout exitoso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<Void>> logout(
            @Parameter(description = "Identificador del usuario que cierra sesion", example = "1")
            @PathVariable Long id) {
        log.info("POST /api/v1/usuarios/{}/logout", id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Logout exitoso"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza la informacion de un usuario existente.")
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
        log.info("PUT /api/v1/usuarios/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.actualizarUsuario(id, dto), "Usuario actualizado"));
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
        log.info("DELETE /api/v1/usuarios/{}", id);
        service.eliminarUsuario(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuario eliminado"));
    }
}
