package cl.duocuc.aduana_api.controller;

import cl.duocuc.aduana_api.dto.ApiResponse;
import cl.duocuc.aduana_api.dto.DocumentoRequestDTO;
import cl.duocuc.aduana_api.dto.DocumentoResponseDTO;
import cl.duocuc.aduana_api.service.DocumentoService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos")
@Tag(name = "Documentos", description = "Gestion de documentos aduaneros asociados a pasajeros")
public class DocumentoController {

    private static final Logger log = LoggerFactory.getLogger(DocumentoController.class);

    private final DocumentoService service;

    public DocumentoController(DocumentoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar documentos", description = "Obtiene todos los documentos registrados en el sistema.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Documentos obtenidos exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<DocumentoResponseDTO>>> listarTodos() {
        log.info("GET /api/v1/documentos - Listando todos los documentos");
        List<DocumentoResponseDTO> documentos = service.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.ok(documentos, "Documentos obtenidos"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar documento por id", description = "Recupera un documento especifico usando su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Documento encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Documento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<DocumentoResponseDTO>> buscarPorId(
            @Parameter(description = "Identificador unico del documento", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/v1/documentos/{} - Buscando documento", id);
        DocumentoResponseDTO documento = service.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(documento, "Documento encontrado"));
    }

    @GetMapping("/pasajero/{idPasajero}")
    @Operation(summary = "Buscar documentos por pasajero", description = "Lista los documentos asociados a un pasajero registrado.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Documentos del pasajero obtenidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<DocumentoResponseDTO>>> buscarPorPasajero(
            @Parameter(description = "Identificador del pasajero propietario de los documentos", example = "10")
            @PathVariable Long idPasajero) {
        log.info("GET /api/v1/documentos/pasajero/{} - Buscando documentos del pasajero", idPasajero);
        List<DocumentoResponseDTO> documentos = service.buscarPorPasajero(idPasajero);
        return ResponseEntity.ok(ApiResponse.ok(documentos, "Documentos del pasajero obtenidos"));
    }

    @PostMapping
    @Operation(summary = "Registrar documento", description = "Crea un nuevo documento aduanero para un pasajero.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Documento registrado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del documento invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Pasajero no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<DocumentoResponseDTO>> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para registrar un documento del pasajero",
                    required = true)
            @RequestBody @Valid DocumentoRequestDTO dto) {
        log.info("POST /api/v1/documentos - Registrando documento tipo: {}", dto.getTipo());
        DocumentoResponseDTO creado = service.registrarDocumento(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(creado, "Documento registrado exitosamente"));
    }

    @PatchMapping("/{id}/validar")
    @Operation(summary = "Validar documento", description = "Marca un documento como validado dentro del flujo aduanero.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Documento validado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Documento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<DocumentoResponseDTO>> validar(
            @Parameter(description = "Identificador del documento a validar", example = "1")
            @PathVariable Long id) {
        log.info("PATCH /api/v1/documentos/{}/validar - Validando documento", id);
        DocumentoResponseDTO validado = service.validarDocumento(id);
        return ResponseEntity.ok(ApiResponse.ok(validado, "Documento validado exitosamente"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar documento", description = "Actualiza la informacion de un documento existente.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Documento actualizado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del documento invalidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Documento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<DocumentoResponseDTO>> actualizar(
            @Parameter(description = "Identificador del documento a actualizar", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del documento",
                    required = true)
            @RequestBody @Valid DocumentoRequestDTO dto) {
        log.info("PUT /api/v1/documentos/{} - Actualizando documento", id);
        DocumentoResponseDTO actualizado = service.actualizarDocumento(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(actualizado, "Documento actualizado"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar documento", description = "Elimina un documento registrado por su identificador.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Documento eliminado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Documento no encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @Parameter(description = "Identificador del documento a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/v1/documentos/{} - Eliminando documento", id);
        service.eliminarDocumento(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Documento eliminado"));
    }
}
