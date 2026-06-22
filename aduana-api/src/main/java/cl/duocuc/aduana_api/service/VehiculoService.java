package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.VehiculoRequestDTO;
import cl.duocuc.aduana_api.dto.VehiculoResponseDTO;
import cl.duocuc.aduana_api.exception.DocumentoNotFoundException;
import cl.duocuc.aduana_api.exception.VehiculoNotFoundException;
import cl.duocuc.aduana_api.model.Documento;
import cl.duocuc.aduana_api.model.Vehiculo;
import cl.duocuc.aduana_api.repository.DocumentoRepository;
import cl.duocuc.aduana_api.repository.VehiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VehiculoService {

    private static final Logger log = LoggerFactory.getLogger(VehiculoService.class);

    private final VehiculoRepository vehiculoRepository;
    private final DocumentoRepository documentoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository,
                           DocumentoRepository documentoRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.documentoRepository = documentoRepository;
    }

    public List<VehiculoResponseDTO> obtenerTodos() {
        log.info("Obteniendo lista de todos los vehículos");
        return vehiculoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public VehiculoResponseDTO buscarPorId(Long id) {
        log.info("Buscando vehículo con id: {}", id);
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Vehículo con id {} no encontrado", id);
                    return new VehiculoNotFoundException("Vehículo con id " + id + " no encontrado");
                });
        return toResponseDTO(vehiculo);
    }

    public VehiculoResponseDTO buscarPorPatente(String patente) {
        log.info("Buscando vehículo con patente: {}", patente);
        Vehiculo vehiculo = vehiculoRepository.findByPatente(patente);
        if (vehiculo == null) {
            log.error("Vehículo con patente {} no encontrado", patente);
            throw new VehiculoNotFoundException("Vehículo con patente " + patente + " no encontrado");
        }
        return toResponseDTO(vehiculo);
    }

    public VehiculoResponseDTO registrarVehiculo(VehiculoRequestDTO dto) {
        log.info("Registrando vehículo con patente: {}", dto.getPatente());

        if (vehiculoRepository.existsByPatente(dto.getPatente())) {
            log.error("Ya existe un vehículo con patente: {}", dto.getPatente());
            throw new IllegalArgumentException("Ya existe un vehículo con patente " + dto.getPatente());
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente(dto.getPatente());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setFechaAdmision(dto.getFechaAdmision());

        // Asociar documento si se envió
        if (dto.getIdDocumento() != null) {
            Documento documento = documentoRepository.findById(dto.getIdDocumento())
                    .orElseThrow(() -> new DocumentoNotFoundException(
                            "Documento con id " + dto.getIdDocumento() + " no encontrado"));
            vehiculo.setDocumento(documento);
        }

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        log.info("Vehículo registrado con id: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    // Regla de negocio clave: generar SATVA
    public VehiculoResponseDTO generarSATVA(Long id) {
        log.info("Generando SATVA para vehículo con id: {}", id);
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNotFoundException(
                        "Vehículo con id " + id + " no encontrado"));

        if (vehiculo.getDocumento() == null) {
            log.error("El vehículo con id {} no tiene documento asociado", id);
            throw new IllegalArgumentException("El vehículo no tiene documento asociado para generar SATVA");
        }

        if (!vehiculo.getDocumento().isEstadoValidacion()) {
            log.error("El documento del vehículo con id {} no está validado", id);
            throw new IllegalArgumentException("El documento debe estar validado antes de generar el SATVA");
        }

        log.info("SATVA generado exitosamente para vehículo con patente: {}", vehiculo.getPatente());
        return toResponseDTO(vehiculo);
    }

    public VehiculoResponseDTO actualizarVehiculo(Long id, VehiculoRequestDTO dto) {
        log.info("Actualizando vehículo con id: {}", id);
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNotFoundException(
                        "Vehículo con id " + id + " no encontrado"));

        vehiculo.setTipo(dto.getTipo());
        vehiculo.setFechaAdmision(dto.getFechaAdmision());

        Vehiculo actualizado = vehiculoRepository.save(vehiculo);
        log.info("Vehículo con id {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public void eliminarVehiculo(Long id) {
        log.info("Eliminando vehículo con id: {}", id);
        if (!vehiculoRepository.existsById(id)) {
            log.error("No se puede eliminar: vehículo con id {} no existe", id);
            throw new VehiculoNotFoundException("Vehículo con id " + id + " no encontrado");
        }
        vehiculoRepository.deleteById(id);
        log.info("Vehículo con id {} eliminado exitosamente", id);
    }

    private VehiculoResponseDTO toResponseDTO(Vehiculo v) {
        return new VehiculoResponseDTO(
                v.getId(),
                v.getPatente(),
                v.getTipo(),
                v.getFechaAdmision(),
                v.generarSATVA(),
                v.getDocumento() != null ? v.getDocumento().getId() : null
        );
    }
}