package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.PasajeroRequestDTO;
import cl.duocuc.aduana_api.dto.PasajeroResponseDTO;
import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.service.PasajeroService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pasajeros")
public class PasajeroController {

    private static final Logger log = LoggerFactory.getLogger(PasajeroController.class);

    private final PasajeroService service;

    public PasajeroController(PasajeroService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PasajeroResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/pasajeros - Listando todos los pasajeros");
        List<PasajeroResponseDTO> pasajeros = service.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.ok(pasajeros, "Pasajeros obtenidos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PasajeroResponseDTO>> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/pasajeros/{} - Buscando pasajero", id);
        PasajeroResponseDTO pasajero = service.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(pasajero, "Pasajero encontrado"));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<ApiResponse<PasajeroResponseDTO>> buscarPorRut(@PathVariable String rut) {
        log.info("GET /api/v1/pasajeros/rut/{} - Buscando pasajero por RUT", rut);
        PasajeroResponseDTO pasajero = service.buscarPorRut(rut);
        return ResponseEntity.ok(ApiResponse.ok(pasajero, "Pasajero encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PasajeroResponseDTO>> registrar(
            @RequestBody @Valid PasajeroRequestDTO dto) {
        log.info("POST /api/v1/pasajeros - Registrando pasajero con RUT: {}", dto.getRut());
        PasajeroResponseDTO creado = service.registrarPasajero(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(creado, "Pasajero registrado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PasajeroResponseDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid PasajeroRequestDTO dto) {
        log.info("PUT /api/v1/pasajeros/{} - Actualizando pasajero", id);
        PasajeroResponseDTO actualizado = service.actualizarPasajero(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(actualizado, "Pasajero actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/pasajeros/{} - Eliminando pasajero", id);
        service.eliminarPasajero(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Pasajero eliminado"));
    }
}