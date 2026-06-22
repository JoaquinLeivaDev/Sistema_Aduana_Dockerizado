package cl.duocuc.aduana_usuarios_api.controller;

import cl.duocuc.aduana_usuarios_api.dto.ApiResponse;
import cl.duocuc.aduana_usuarios_api.dto.RolRequestDTO;
import cl.duocuc.aduana_usuarios_api.dto.RolResponseDTO;
import cl.duocuc.aduana_usuarios_api.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Gestión de roles de usuario")
public class RolController {

    private final RolService rolService;

    @GetMapping
    @Operation(summary = "Lista todos los roles")
    public ResponseEntity<ApiResponse<List<RolResponseDTO>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok(rolService.obtenerTodos(), "Roles obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un rol por id")
    public ResponseEntity<ApiResponse<RolResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(rolService.buscarPorId(id), "Rol encontrado"));
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo rol")
    public ResponseEntity<ApiResponse<RolResponseDTO>> crear(@RequestBody @Valid RolRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(rolService.crear(dto), "Rol creado"));
    }
}
