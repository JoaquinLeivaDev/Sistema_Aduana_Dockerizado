package cl.duocuc.aduana_api.exception;

import cl.duocuc.aduana_api.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Maneja errores de validación @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidacion(MethodArgumentNotValidException ex) {
        String errores = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("Error de validación: {}", errores);
        return ResponseEntity.badRequest().body(ApiResponse.error(errores));
    }

    // Maneja pasajero no encontrado → 404
    @ExceptionHandler(PasajeroNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(PasajeroNotFoundException ex) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    // Maneja RUT duplicado → 409
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflicto(IllegalArgumentException ex) {
        log.error("Conflicto: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
    }

    // Maneja cualquier error no esperado → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        log.error("Error interno: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno del servidor"));
    }

    @ExceptionHandler(DocumentoNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDocumentoNotFound(DocumentoNotFoundException ex) {
        log.error("Documento no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(VehiculoNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleVehiculoNotFound(VehiculoNotFoundException ex) {
        log.error("Vehículo no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        log.error("Usuario no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(RolNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRolNotFound(RolNotFoundException ex) {
        log.error("Rol no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(TurnoNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTurnoNotFound(TurnoNotFoundException ex) {
        log.error("Turno no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ReporteNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleReporteNotFound(ReporteNotFoundException ex) {
        log.error("Reporte no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }
}