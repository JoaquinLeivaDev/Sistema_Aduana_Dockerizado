package cl.duocuc.aduana_usuarios_api.service;

import cl.duocuc.aduana_usuarios_api.dto.RolRequestDTO;
import cl.duocuc.aduana_usuarios_api.dto.RolResponseDTO;
import cl.duocuc.aduana_usuarios_api.exception.RolNotFoundException;
import cl.duocuc.aduana_usuarios_api.model.Rol;
import cl.duocuc.aduana_usuarios_api.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private static final Logger log = LoggerFactory.getLogger(RolService.class);

    private final RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<RolResponseDTO> obtenerTodos() {
        return rolRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public RolResponseDTO buscarPorId(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RolNotFoundException("Rol con id " + id + " no encontrado"));
        return toResponseDTO(rol);
    }

    @Transactional
    public RolResponseDTO crear(RolRequestDTO dto) {
        // Regla de negocio: no se permiten roles duplicados por nombre
        if (rolRepository.existsByNombre(dto.getNombre())) {
            log.error("Ya existe un rol con nombre: {}", dto.getNombre());
            throw new IllegalArgumentException("Ya existe un rol con el nombre " + dto.getNombre());
        }
        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        Rol guardado = rolRepository.save(rol);
        log.info("Rol creado con id: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    private RolResponseDTO toResponseDTO(Rol rol) {
        return new RolResponseDTO(rol.getId(), rol.getNombre());
    }
}
