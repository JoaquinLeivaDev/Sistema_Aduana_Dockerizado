package cl.duocuc.aduana_usuarios_api.controller;

import cl.duocuc.aduana_usuarios_api.dto.*;
import cl.duocuc.aduana_usuarios_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios y autenticación")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Lista todos los usuarios registrados")
    public ResponseEntity<ApiResponse<List<UsuarioResponseDTO>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.obtenerTodos(), "Usuarios obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un usuario por id")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.buscarPorId(id), "Usuario encontrado"));
    }

    @PostMapping
    @Operation(summary = "Registra un nuevo usuario (la contraseña se almacena hasheada)")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> registrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(usuarioService.registrarUsuario(dto), "Usuario registrado"));
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica un usuario por username/password")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.login(dto), "Login exitoso"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza username/password/rol de un usuario")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> actualizar(
            @PathVariable Long id, @RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.actualizarUsuario(id, dto), "Usuario actualizado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un usuario")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuario eliminado"));
    }

    @GetMapping("/{id}/reportes")
    @Operation(summary = "Obtiene los reportes asociados a un usuario (consulta a aduana-reportes-api)")
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> obtenerReportes(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.obtenerReportesPorUsuario(id), "Reportes del usuario obtenidos"));
    }
}
