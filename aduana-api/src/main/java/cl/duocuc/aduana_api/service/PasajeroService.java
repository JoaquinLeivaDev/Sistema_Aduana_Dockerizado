package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.PasajeroRequestDTO;
import cl.duocuc.aduana_api.dto.PasajeroResponseDTO;
import cl.duocuc.aduana_api.exception.PasajeroNotFoundException;
import cl.duocuc.aduana_api.model.Pasajero;
import cl.duocuc.aduana_api.repository.PasajeroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PasajeroService {

    private static final Logger log = LoggerFactory.getLogger(PasajeroService.class);

    private final PasajeroRepository repository;

    public PasajeroService(PasajeroRepository repository) {
        this.repository = repository;
    }

    public List<PasajeroResponseDTO> obtenerTodos() {
        log.info("Obteniendo lista de todos los pasajeros");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PasajeroResponseDTO buscarPorId(Long id) {
        log.info("Buscando pasajero con id: {}", id);
        Pasajero pasajero = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pasajero con id {} no encontrado", id);
                    return new PasajeroNotFoundException("Pasajero con id " + id + " no encontrado");
                });
        return toResponseDTO(pasajero);
    }

    public PasajeroResponseDTO buscarPorRut(String rut) {
        log.info("Buscando pasajero con RUT: {}", rut);
        Pasajero pasajero = repository.findByRut(rut);
        if (pasajero == null) {
            log.error("Pasajero con RUT {} no encontrado", rut);
            throw new PasajeroNotFoundException("Pasajero con RUT " + rut + " no encontrado");
        }
        return toResponseDTO(pasajero);
    }

    public PasajeroResponseDTO registrarPasajero(PasajeroRequestDTO dto) {
        log.info("Registrando nuevo pasajero con RUT: {}", dto.getRut());

        if (repository.existsByRut(dto.getRut())) {
            log.error("Ya existe un pasajero con RUT: {}", dto.getRut());
            throw new IllegalArgumentException("Ya existe un pasajero con el RUT " + dto.getRut());
        }

        Pasajero pasajero = new Pasajero();
        pasajero.setRut(dto.getRut());
        pasajero.setNombre(dto.getNombre());
        pasajero.setApellidos(dto.getApellidos());
        pasajero.setFechaNac(dto.getFechaNac());
        pasajero.setCorreo(dto.getCorreo());

        Pasajero guardado = repository.save(pasajero);
        log.info("Pasajero registrado con id: {}", guardado.getIdPasajero());
        return toResponseDTO(guardado);
    }

    public PasajeroResponseDTO actualizarPasajero(Long id, PasajeroRequestDTO dto) {
        log.info("Actualizando pasajero con id: {}", id);
        Pasajero pasajero = repository.findById(id)
                .orElseThrow(() -> new PasajeroNotFoundException("Pasajero con id " + id + " no encontrado"));

        pasajero.setNombre(dto.getNombre());
        pasajero.setApellidos(dto.getApellidos());
        pasajero.setFechaNac(dto.getFechaNac());
        pasajero.setCorreo(dto.getCorreo());

        Pasajero actualizado = repository.save(pasajero);
        log.info("Pasajero con id {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public void eliminarPasajero(Long id) {
        log.info("Eliminando pasajero con id: {}", id);
        if (!repository.existsById(id)) {
            log.error("No se puede eliminar: pasajero con id {} no existe", id);
            throw new PasajeroNotFoundException("Pasajero con id " + id + " no encontrado");
        }
        repository.deleteById(id);
        log.info("Pasajero con id {} eliminado exitosamente", id);
    }

    // Método privado de conversión entidad → DTO
    private PasajeroResponseDTO toResponseDTO(Pasajero p) {
        return new PasajeroResponseDTO(
                p.getIdPasajero(),
                p.getRut(),
                p.getNombre(),
                p.getApellidos(),
                p.getFechaNac(),
                p.getCorreo(),
                p.calcularEdad()
        );
    }
}