package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.DocumentoRequestDTO;
import cl.duocuc.aduana_api.dto.DocumentoResponseDTO;
import cl.duocuc.aduana_api.exception.DocumentoNotFoundException;
import cl.duocuc.aduana_api.exception.PasajeroNotFoundException;
import cl.duocuc.aduana_api.model.Documento;
import cl.duocuc.aduana_api.model.Pasajero;
import cl.duocuc.aduana_api.repository.DocumentoRepository;
import cl.duocuc.aduana_api.repository.PasajeroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DocumentoService {

    private static final Logger log = LoggerFactory.getLogger(DocumentoService.class);

    private final DocumentoRepository documentoRepository;
    private final PasajeroRepository pasajeroRepository;

    public DocumentoService(DocumentoRepository documentoRepository,
                            PasajeroRepository pasajeroRepository) {
        this.documentoRepository = documentoRepository;
        this.pasajeroRepository = pasajeroRepository;
    }

    public List<DocumentoResponseDTO> obtenerTodos() {
        log.info("Obteniendo lista de todos los documentos");
        return documentoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public DocumentoResponseDTO buscarPorId(Long id) {
        log.info("Buscando documento con id: {}", id);
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Documento con id {} no encontrado", id);
                    return new DocumentoNotFoundException("Documento con id " + id + " no encontrado");
                });
        return toResponseDTO(documento);
    }

    public List<DocumentoResponseDTO> buscarPorPasajero(Long idPasajero) {
        log.info("Buscando documentos del pasajero con id: {}", idPasajero);
        if (!pasajeroRepository.existsById(idPasajero)) {
            throw new PasajeroNotFoundException("Pasajero con id " + idPasajero + " no encontrado");
        }
        return documentoRepository.findByPasajeroIdPasajero(idPasajero)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public DocumentoResponseDTO registrarDocumento(DocumentoRequestDTO dto) {
        log.info("Registrando documento tipo: {} para pasajero id: {}",
                dto.getTipo(), dto.getIdPasajero());

        Pasajero pasajero = pasajeroRepository.findById(dto.getIdPasajero())
                .orElseThrow(() -> new PasajeroNotFoundException(
                        "Pasajero con id " + dto.getIdPasajero() + " no encontrado"));

        Documento documento = new Documento();
        documento.setTipo(dto.getTipo());
        documento.setUrlArchivo(dto.getUrlArchivo());
        documento.setEstadoValidacion(false); // por defecto sin validar
        documento.setPasajero(pasajero);

        Documento guardado = documentoRepository.save(documento);
        log.info("Documento registrado con id: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public DocumentoResponseDTO validarDocumento(Long id) {
        log.info("Validando documento con id: {}", id);
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new DocumentoNotFoundException(
                        "Documento con id " + id + " no encontrado"));

        documento.setEstadoValidacion(true);
        Documento validado = documentoRepository.save(documento);
        log.info("Documento con id {} validado exitosamente", id);
        return toResponseDTO(validado);
    }

    public DocumentoResponseDTO actualizarDocumento(Long id, DocumentoRequestDTO dto) {
        log.info("Actualizando documento con id: {}", id);
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new DocumentoNotFoundException(
                        "Documento con id " + id + " no encontrado"));

        documento.setTipo(dto.getTipo());
        documento.setUrlArchivo(dto.getUrlArchivo());

        Documento actualizado = documentoRepository.save(documento);
        log.info("Documento con id {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public void eliminarDocumento(Long id) {
        log.info("Eliminando documento con id: {}", id);
        if (!documentoRepository.existsById(id)) {
            log.error("No se puede eliminar: documento con id {} no existe", id);
            throw new DocumentoNotFoundException("Documento con id " + id + " no encontrado");
        }
        documentoRepository.deleteById(id);
        log.info("Documento con id {} eliminado exitosamente", id);
    }

    private DocumentoResponseDTO toResponseDTO(Documento d) {
        return new DocumentoResponseDTO(
                d.getId(),
                d.getTipo(),
                d.getUrlArchivo(),
                d.isEstadoValidacion(),
                d.getPasajero() != null ? d.getPasajero().getIdPasajero() : null
        );
    }
}