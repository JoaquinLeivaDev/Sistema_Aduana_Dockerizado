package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.VehiculoRequestDTO;
import cl.duocuc.aduana_api.dto.VehiculoResponseDTO;
import cl.duocuc.aduana_api.service.VehiculoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
public class VehiculoController {

    private static final Logger log = LoggerFactory.getLogger(VehiculoController.class);

    private final VehiculoService service;

    public VehiculoController(VehiculoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehiculoResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/vehiculos - Listando todos los vehículos");
        return ResponseEntity.ok(ApiResponse.ok(service.obtenerTodos(), "Vehículos obtenidos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/vehiculos/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorId(id), "Vehículo encontrado"));
    }

    @GetMapping("/patente/{patente}")
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> buscarPorPatente(
            @PathVariable String patente) {
        log.info("GET /api/v1/vehiculos/patente/{}", patente);
        return ResponseEntity.ok(ApiResponse.ok(service.buscarPorPatente(patente), "Vehículo encontrado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> registrar(
            @RequestBody @Valid VehiculoRequestDTO dto) {
        log.info("POST /api/v1/vehiculos - Registrando vehículo patente: {}", dto.getPatente());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(service.registrarVehiculo(dto), "Vehículo registrado"));
    }

    // Endpoint clave del dominio: generar SATVA
    @PatchMapping("/{id}/satva")
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> generarSATVA(@PathVariable Long id) {
        log.info("PATCH /api/v1/vehiculos/{}/satva - Generando SATVA", id);
        return ResponseEntity.ok(ApiResponse.ok(service.generarSATVA(id), "SATVA generado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid VehiculoRequestDTO dto) {
        log.info("PUT /api/v1/vehiculos/{}", id);
        return ResponseEntity.ok(ApiResponse.ok(service.actualizarVehiculo(id, dto), "Vehículo actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/vehiculos/{}", id);
        service.eliminarVehiculo(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Vehículo eliminado"));
    }
}