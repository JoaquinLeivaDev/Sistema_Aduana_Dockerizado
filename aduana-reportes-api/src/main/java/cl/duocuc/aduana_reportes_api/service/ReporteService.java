package cl.duocuc.aduana_reportes_api.service;

import cl.duocuc.aduana_reportes_api.client.AduanaClient;
import cl.duocuc.aduana_reportes_api.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final AduanaClient aduanaClient;

    // ──────────────────────────────────────────────
    // Reglas de negocio
    // ──────────────────────────────────────────────

    // Regla: el ID debe ser un número positivo
    public void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
    }

    // Regla: el tipo de reporte no puede estar vacío ni superar 50 caracteres
    public void validarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de reporte no puede estar vacío");
        }
        if (tipo.trim().length() > 50) {
            throw new IllegalArgumentException("El tipo de reporte no puede superar 50 caracteres");
        }
    }

    // Regla: la fecha del reporte no puede ser futura
    public void validarFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del reporte es obligatoria");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del reporte no puede ser una fecha futura");
        }
    }

    // Regla: el idUsuario debe ser positivo
    public void validarIdUsuario(Long idUsuario) {
        if (idUsuario == null || idUsuario <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo");
        }
    }

    // ──────────────────────────────────────────────
    // Operaciones del servicio
    // ──────────────────────────────────────────────

    public ApiResponse<List<ReporteResponseDTO>> obtenerTodos() {
        log.info("Solicitando lista de reportes al core");
        return aduanaClient.obtenerReportes();
    }

    public ApiResponse<ReporteResponseDTO> buscarPorId(Long id) {
        validarId(id);
        log.info("Buscando reporte con id: {}", id);
        return aduanaClient.obtenerReportePorId(id);
    }

    public ApiResponse<List<ReporteResponseDTO>> obtenerPorUsuario(Long idUsuario) {
        validarIdUsuario(idUsuario);
        log.info("Buscando reportes del usuario con id: {}", idUsuario);
        return aduanaClient.obtenerReportesPorUsuario(idUsuario);
    }

    public ApiResponse<List<PasajeroResponse>> generarReportePasajeros() {
        log.info("Generando reporte consolidado de pasajeros");
        return aduanaClient.obtenerPasajeros();
    }

    public ApiResponse<ReporteResponseDTO> registrarReporte(ReporteRequestDTO dto) {
        log.info("Validando datos antes de registrar reporte tipo: {}", dto.getTipo());
        validarTipo(dto.getTipo());
        validarFecha(dto.getFecha());
        validarIdUsuario(dto.getIdUsuario());
        log.info("Datos válidos, enviando reporte al core");
        return aduanaClient.registrarReporte(dto);
    }

    public ApiResponse<Void> eliminarReporte(Long id) {
        validarId(id);
        log.info("Eliminando reporte con id: {}", id);
        return aduanaClient.eliminarReporte(id);
    }
}