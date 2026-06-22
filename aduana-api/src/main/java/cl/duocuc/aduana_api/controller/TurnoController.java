package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.TurnoRequestDTO;
import cl.duocuc.aduana_api.dto.TurnoResponseDTO;
import cl.duocuc.aduana_api.service.TurnoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/turnos")
public class TurnoController {

    private static final Logger log = LoggerFactory.getLogger(TurnoController.class);

    private final TurnoService service;

    public TurnoController(TurnoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TurnoResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/turnos - Listando turnos");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Turnos obtenidos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/turnos/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Turno encontrado"));
    }

    @GetMapping("/pasajero/{idPasajero}")
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> buscarPorPasajero(
            @PathVariable Long idPasajero) {
        log.info("GET /api/v1/turnos/pasajero/{}", idPasajero);
        return ResponseEntity.ok(ApiResponse.ok(
                service.buscarPorPasajero(idPasajero), "Turno del pasajero encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> registrar(
            @RequestBody @Valid TurnoRequestDTO dto) {
        log.info("POST /api/v1/turnos - Registrando turno para pasajero: {}", dto.getIdPasajero());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrarTurno(dto), "Turno registrado"));
    }

    // Endpoint clave del dominio: asignar ventanilla
    @PatchMapping("/{id}/ventanilla/{numero}")
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> asignarVentanilla(
            @PathVariable Long id,
            @PathVariable Integer numero) {
        log.info("PATCH /api/v1/turnos/{}/ventanilla/{}", id, numero);
        return ResponseEntity.ok(ApiResponse.ok(
                service.asignarVentanilla(id, numero), "Ventanilla asignada exitosamente"));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<TurnoResponseDTO>> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        log.info("PATCH /api/v1/turnos/{}/estado - nuevo estado: {}", id, estado);
        return ResponseEntity.ok(ApiResponse.ok(
                service.actualizarEstado(id, estado), "Estado actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/turnos/{}", id);
        service.eliminarTurno(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Turno eliminado"));
    }
}