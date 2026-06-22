package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.ReporteRequestDTO;
import cl.duocuc.aduana_api.dto.ReporteResponseDTO;
import cl.duocuc.aduana_api.exception.ReporteNotFoundException;
import cl.duocuc.aduana_api.exception.UsuarioNotFoundException;
import cl.duocuc.aduana_api.model.Reporte;
import cl.duocuc.aduana_api.model.Usuario;
import cl.duocuc.aduana_api.repository.ReporteRepository;
import cl.duocuc.aduana_api.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    public ReporteService(ReporteRepository reporteRepository,
                          UsuarioRepository usuarioRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ReporteResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los reportes");
        return reporteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ReporteResponseDTO buscarPorId(Long id) {
        log.info("Buscando reporte con id: {}", id);
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reporte con id {} no encontrado", id);
                    return new ReporteNotFoundException("Reporte con id " + id + " no encontrado");
                });
        return toResponseDTO(reporte);
    }

    public List<ReporteResponseDTO> obtenerPorUsuario(Long idUsuario) {
        log.info("Obteniendo reportes del usuario con id: {}", idUsuario);
        return reporteRepository.findByUsuario_Id(idUsuario)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ReporteResponseDTO> obtenerPorTipo(String tipo) {
        log.info("Obteniendo reportes de tipo: {}", tipo);
        return reporteRepository.findByTipo(tipo)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ReporteResponseDTO registrar(ReporteRequestDTO dto) {
        log.info("Registrando reporte tipo: {} para usuario id: {}", dto.getTipo(), dto.getIdUsuario());
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> {
                    log.error("Usuario con id {} no encontrado", dto.getIdUsuario());
                    return new UsuarioNotFoundException("Usuario con id " + dto.getIdUsuario() + " no encontrado");
                });

        Reporte reporte = new Reporte();
        reporte.setTipo(dto.getTipo());
        reporte.setFecha(dto.getFecha());
        reporte.setDatos(dto.getDatos());
        reporte.setUsuario(usuario);

        Reporte guardado = reporteRepository.save(reporte);
        log.info("Reporte registrado con id: {}", guardado.getIdReporte());
        return toResponseDTO(guardado);
    }

    public void eliminar(Long id) {
        log.info("Eliminando reporte con id: {}", id);
        if (!reporteRepository.existsById(id)) {
            log.error("Reporte con id {} no encontrado", id);
            throw new ReporteNotFoundException("Reporte con id " + id + " no encontrado");
        }
        reporteRepository.deleteById(id);
        log.info("Reporte con id {} eliminado", id);
    }

    private ReporteResponseDTO toResponseDTO(Reporte r) {
        return new ReporteResponseDTO(
                r.getIdReporte(),
                r.getTipo(),
                r.getFecha(),
                r.getDatos(),
                r.getUsuario().getId()
        );
    }
}