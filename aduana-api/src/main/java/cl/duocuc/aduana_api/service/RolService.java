package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.RolRequestDTO;
import cl.duocuc.aduana_api.dto.RolResponseDTO;
import cl.duocuc.aduana_api.exception.RolNotFoundException;
import cl.duocuc.aduana_api.model.Rol;
import cl.duocuc.aduana_api.repository.RolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolService {

    private static final Logger log = LoggerFactory.getLogger(RolService.class);

    private final RolRepository repository;

    public RolService(RolRepository repository) {
        this.repository = repository;
    }

    public List<RolResponseDTO> obtenerTodos() {
        log.info("Obteniendo lista de todos los roles");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public RolResponseDTO buscarPorId(Long id) {
        log.info("Buscando rol con id: {}", id);
        Rol rol = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Rol con id {} no encontrado", id);
                    return new RolNotFoundException("Rol con id " + id + " no encontrado");
                });
        return toResponseDTO(rol);
    }

    public RolResponseDTO registrarRol(RolRequestDTO dto) {
        log.info("Registrando rol: {}", dto.getNombre());

        if (repository.existsByNombre(dto.getNombre())) {
            log.error("Ya existe un rol con nombre: {}", dto.getNombre());
            throw new IllegalArgumentException("Ya existe un rol con el nombre " + dto.getNombre());
        }

        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());

        Rol guardado = repository.save(rol);
        log.info("Rol registrado con id: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public RolResponseDTO actualizarRol(Long id, RolRequestDTO dto) {
        log.info("Actualizando rol con id: {}", id);
        Rol rol = repository.findById(id)
                .orElseThrow(() -> new RolNotFoundException(
                        "Rol con id " + id + " no encontrado"));

        rol.setNombre(dto.getNombre());

        Rol actualizado = repository.save(rol);
        log.info("Rol con id {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public void eliminarRol(Long id) {
        log.info("Eliminando rol con id: {}", id);
        if (!repository.existsById(id)) {
            log.error("No se puede eliminar: rol con id {} no existe", id);
            throw new RolNotFoundException("Rol con id " + id + " no encontrado");
        }
        repository.deleteById(id);
        log.info("Rol con id {} eliminado exitosamente", id);
    }

    private RolResponseDTO toResponseDTO(Rol r) {
        return new RolResponseDTO(r.getId(), r.getNombre());
    }
}