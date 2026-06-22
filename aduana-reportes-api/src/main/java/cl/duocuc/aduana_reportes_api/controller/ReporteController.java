package cl.duocuc.aduana_reportes_api.controller;

import cl.duocuc.aduana_reportes_api.dto.*;
import cl.duocuc.aduana_reportes_api.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> listarTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReporteResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<ReporteResponseDTO>>> buscarPorUsuario(
            @PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.obtenerPorUsuario(idUsuario));
    }

    @GetMapping("/consolidado/pasajeros")
    public ResponseEntity<ApiResponse<List<PasajeroResponse>>> reportePasajeros() {
        return ResponseEntity.ok(service.generarReportePasajeros());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReporteResponseDTO>> registrar(
            @RequestBody @Valid ReporteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrarReporte(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        return ResponseEntity.ok(service.eliminarReporte(id));
    }
}