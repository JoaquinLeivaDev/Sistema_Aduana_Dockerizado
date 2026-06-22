package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.*;
import cl.duocuc.aduana_api.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/usuarios - Listando usuarios");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Usuarios obtenidos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/usuarios/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Usuario encontrado"));
    }

    @GetMapping("/{id}/reportes")
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> obtenerReportes(@PathVariable Long id) {
        log.info("GET /api/v1/usuarios/{}/reportes", id);
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerReportesPorUsuario(id), "Reportes del usuario obtenidos"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> registrar(
            @RequestBody @Valid UsuarioRequestDTO dto) {
        log.info("POST /api/v1/usuarios - Registrando usuario: {}", dto.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrarUsuario(dto), "Usuario registrado"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> login(
            @RequestBody @Valid LoginRequestDTO dto) {
        log.info("POST /api/v1/usuarios/login - Login: {}", dto.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(service.login(dto), "Login exitoso"));
    }

    @PostMapping("/{id}/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@PathVariable Long id) {
        log.info("POST /api/v1/usuarios/{}/logout", id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Logout exitoso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioRequestDTO dto) {
        log.info("PUT /api/v1/usuarios/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.actualizarUsuario(id, dto), "Usuario actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/usuarios/{}", id);
        service.eliminarUsuario(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Usuario eliminado"));
    }


}