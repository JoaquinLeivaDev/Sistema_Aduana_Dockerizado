package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.DocumentoRequestDTO;
import cl.duocuc.aduana_api.dto.DocumentoResponseDTO;
import cl.duocuc.aduana_api.service.DocumentoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos")
public class DocumentoController {

    private static final Logger log = LoggerFactory.getLogger(DocumentoController.class);

    private final DocumentoService service;

    public DocumentoController(DocumentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentoResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/documentos - Listando todos los documentos");
        List<DocumentoResponseDTO> documentos = service.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.ok(documentos, "Documentos obtenidos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentoResponseDTO>> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/documentos/{} - Buscando documento", id);
        DocumentoResponseDTO documento = service.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(documento, "Documento encontrado"));
    }

    // Endpoint clave del dominio: buscar documentos por pasajero
    @GetMapping("/pasajero/{idPasajero}")
    public ResponseEntity<ApiResponse<List<DocumentoResponseDTO>>> buscarPorPasajero(
            @PathVariable Long idPasajero) {
        log.info("GET /api/v1/documentos/pasajero/{} - Buscando documentos del pasajero", idPasajero);
        List<DocumentoResponseDTO> documentos = service.buscarPorPasajero(idPasajero);
        return ResponseEntity.ok(ApiResponse.ok(documentos, "Documentos del pasajero obtenidos"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentoResponseDTO>> registrar(
            @RequestBody @Valid DocumentoRequestDTO dto) {
        log.info("POST /api/v1/documentos - Registrando documento tipo: {}", dto.getTipo());
        DocumentoResponseDTO creado = service.registrarDocumento(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(creado, "Documento registrado exitosamente"));
    }

    // Endpoint clave del dominio: validar un documento
    @PatchMapping("/{id}/validar")
    public ResponseEntity<ApiResponse<DocumentoResponseDTO>> validar(@PathVariable Long id) {
        log.info("PATCH /api/v1/documentos/{}/validar - Validando documento", id);
        DocumentoResponseDTO validado = service.validarDocumento(id);
        return ResponseEntity.ok(ApiResponse.ok(validado, "Documento validado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentoResponseDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid DocumentoRequestDTO dto) {
        log.info("PUT /api/v1/documentos/{} - Actualizando documento", id);
        DocumentoResponseDTO actualizado = service.actualizarDocumento(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(actualizado, "Documento actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/documentos/{} - Eliminando documento", id);
        service.eliminarDocumento(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Documento eliminado"));
    }
}