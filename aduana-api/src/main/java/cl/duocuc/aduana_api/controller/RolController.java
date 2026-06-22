package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.RolRequestDTO;
import cl.duocuc.aduana_api.dto.RolResponseDTO;
import cl.duocuc.aduana_api.service.RolService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RolController {

    private static final Logger log = LoggerFactory.getLogger(RolController.class);

    private final RolService service;

    public RolController(RolService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RolResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/roles - Listando roles");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Roles obtenidos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RolResponseDTO>> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/roles/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Rol encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RolResponseDTO>> registrar(
            @RequestBody @Valid RolRequestDTO dto) {
        log.info("POST /api/v1/roles - Registrando rol: {}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrarRol(dto), "Rol registrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RolResponseDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid RolRequestDTO dto) {
        log.info("PUT /api/v1/roles/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.actualizarRol(id, dto), "Rol actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/roles/{}", id);
        service.eliminarRol(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Rol eliminado"));
    }
}