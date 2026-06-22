package cl.duocuc.aduana_usuarios_api.service;

import cl.duocuc.aduana_usuarios_api.client.ReportesClient;
import cl.duocuc.aduana_usuarios_api.dto.*;
import cl.duocuc.aduana_usuarios_api.exception.CredencialesInvalidasException;
import cl.duocuc.aduana_usuarios_api.exception.RolNotFoundException;
import cl.duocuc.aduana_usuarios_api.exception.UsuarioNotFoundException;
import cl.duocuc.aduana_usuarios_api.model.Rol;
import cl.duocuc.aduana_usuarios_api.model.Usuario;
import cl.duocuc.aduana_usuarios_api.repository.RolRepository;
import cl.duocuc.aduana_usuarios_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReportesClient reportesClient;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodos() {
        log.info("Obteniendo lista de todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        log.info("Buscando usuario con id: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario con id " + id + " no encontrado"));
        return toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto) {
        log.info("Registrando usuario: {}", dto.getUsername());

        // Regla de negocio: username único
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            log.error("Ya existe un usuario con username: {}", dto.getUsername());
            throw new IllegalArgumentException("Ya existe un usuario con el username " + dto.getUsername());
        }

        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new RolNotFoundException("Rol con id " + dto.getIdRol() + " no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        // Regla de negocio: la contraseña nunca se persiste en texto plano
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rol);

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado con id: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO login(LoginRequestDTO dto) {
        log.info("Intento de login para usuario: {}", dto.getUsername());

        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername());
        if (usuario == null) {
            log.error("Login fallido: usuario {} no existe", dto.getUsername());
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPasswordHash())) {
            log.error("Login fallido: contraseña incorrecta para {}", dto.getUsername());
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        log.info("Login exitoso para usuario: {}", dto.getUsername());
        return toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        log.info("Actualizando usuario con id: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario con id " + id + " no encontrado"));

        usuario.setUsername(dto.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        if (dto.getIdRol() != null) {
            Rol rol = rolRepository.findById(dto.getIdRol())
                    .orElseThrow(() -> new RolNotFoundException("Rol con id " + dto.getIdRol() + " no encontrado"));
            usuario.setRol(rol);
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        log.info("Usuario con id {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        if (!usuarioRepository.existsById(id)) {
            log.error("No se puede eliminar: usuario con id {} no existe", id);
            throw new UsuarioNotFoundException("Usuario con id " + id + " no encontrado");
        }
        usuarioRepository.deleteById(id);
        log.info("Usuario con id {} eliminado exitosamente", id);
    }

    /**
     * Comunicación REST hacia aduana-reportes-api (vía Feign).
     * Si el servicio remoto no responde, se degrada de forma controlada
     * en lugar de propagar un error 500 genérico al cliente del Gateway.
     */
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> obtenerReportesPorUsuario(Long id) {
        log.info("Obteniendo reportes del usuario con id: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException("Usuario con id " + id + " no encontrado");
        }
        try {
            ApiResponse<List<ReporteResponseDTO>> respuesta = reportesClient.obtenerReportesPorUsuario(id);
            return respuesta.getData() != null ? respuesta.getData() : Collections.emptyList();
        } catch (Exception ex) {
            log.error("No se pudo contactar a aduana-reportes-api para el usuario {}: {}", id, ex.getMessage());
            return Collections.emptyList();
        }
    }

    private UsuarioResponseDTO toResponseDTO(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getUsername(), u.getRol().getNombre());
    }
}
