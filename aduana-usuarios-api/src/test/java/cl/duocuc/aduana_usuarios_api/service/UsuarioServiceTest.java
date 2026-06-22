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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ReportesClient reportesClient;

    @InjectMocks
    private UsuarioService usuarioService;

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private Rol rolAdmin() {
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");
        return rol;
    }

    private Usuario usuarioJLeiva() {
        Usuario u = new Usuario();
        u.setId(10L);
        u.setUsername("jleiva");
        u.setPasswordHash("hashed123");
        u.setRol(rolAdmin());
        return u;
    }

    // ─── registrarUsuario ─────────────────────────────────────────────────────

    @Test
    void registrarUsuario_ShouldRegisterUser_WhenUsernameIsUnique() {
        // Given
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setUsername("jleiva");
        dto.setPassword("pass123");
        dto.setIdRol(1L);

        when(usuarioRepository.existsByUsername("jleiva")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rolAdmin()));
        when(passwordEncoder.encode("pass123")).thenReturn("hashed123");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioJLeiva());

        // When
        UsuarioResponseDTO resultado = usuarioService.registrarUsuario(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("jleiva", resultado.getUsername());
        verify(passwordEncoder).encode("pass123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_ShouldThrowException_WhenUsernameAlreadyExists() {
        // Given
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setUsername("jleiva");
        dto.setPassword("pass123");
        dto.setIdRol(1L);

        when(usuarioRepository.existsByUsername("jleiva")).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario(dto));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrarUsuario_ShouldThrowException_WhenRolNotFound() {
        // Given
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setUsername("jleiva");
        dto.setPassword("pass123");
        dto.setIdRol(99L);

        when(usuarioRepository.existsByUsername("jleiva")).thenReturn(false);
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RolNotFoundException.class,
                () -> usuarioService.registrarUsuario(dto));
    }

    // ─── login ────────────────────────────────────────────────────────────────

    @Test
    void login_ShouldReturnUsuario_WhenCredencialesCorrectas() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("jleiva");
        dto.setPassword("pass123");

        when(usuarioRepository.findByUsername("jleiva")).thenReturn(usuarioJLeiva());
        when(passwordEncoder.matches("pass123", "hashed123")).thenReturn(true);

        // When
        UsuarioResponseDTO resultado = usuarioService.login(dto);

        // Then
        assertNotNull(resultado);
        assertEquals("jleiva", resultado.getUsername());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsWrong() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("jleiva");
        dto.setPassword("wrongpass");

        when(usuarioRepository.findByUsername("jleiva")).thenReturn(usuarioJLeiva());
        when(passwordEncoder.matches("wrongpass", "hashed123")).thenReturn(false);

        // When & Then
        assertThrows(CredencialesInvalidasException.class,
                () -> usuarioService.login(dto));
    }

    @Test
    void login_ShouldThrowException_WhenUserDoesNotExist() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("desconocido");
        dto.setPassword("pass123");

        when(usuarioRepository.findByUsername("desconocido")).thenReturn(null);

        // When & Then
        assertThrows(CredencialesInvalidasException.class,
                () -> usuarioService.login(dto));
    }

    // ─── eliminarUsuario ──────────────────────────────────────────────────────

    @Test
    void eliminarUsuario_ShouldDelete_WhenUserExists() {
        // Given
        when(usuarioRepository.existsById(10L)).thenReturn(true);

        // When
        usuarioService.eliminarUsuario(10L);

        // Then
        verify(usuarioRepository).deleteById(10L);
    }

    @Test
    void eliminarUsuario_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(usuarioRepository.existsById(404L)).thenReturn(false);

        // When & Then
        assertThrows(UsuarioNotFoundException.class,
                () -> usuarioService.eliminarUsuario(404L));
        verify(usuarioRepository, never()).deleteById(any());
    }

    // ─── buscarPorId ──────────────────────────────────────────────────────────

    @Test
    void buscarPorId_ShouldReturnUsuario_WhenExists() {
        // Given
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioJLeiva()));

        // When
        UsuarioResponseDTO resultado = usuarioService.buscarPorId(10L);

        // Then
        assertNotNull(resultado);
        assertEquals("jleiva", resultado.getUsername());
    }

    @Test
    void buscarPorId_ShouldThrowException_WhenNotFound() {
        // Given
        when(usuarioRepository.findById(404L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsuarioNotFoundException.class,
                () -> usuarioService.buscarPorId(404L));
    }

    // ─── obtenerReportesPorUsuario ────────────────────────────────────────────

    @Test
    void obtenerReportesPorUsuario_ShouldReturnReportes_WhenClientResponds() {
        // Given
        ReporteResponseDTO reporte = new ReporteResponseDTO();
        ApiResponse<List<ReporteResponseDTO>> respuesta =
                ApiResponse.ok(List.of(reporte), "OK");

        when(usuarioRepository.existsById(10L)).thenReturn(true);
        when(reportesClient.obtenerReportesPorUsuario(10L)).thenReturn(respuesta);

        // When
        List<ReporteResponseDTO> resultado = usuarioService.obtenerReportesPorUsuario(10L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerReportesPorUsuario_ShouldReturnEmptyList_WhenClientFails() {
        // Given
        when(usuarioRepository.existsById(10L)).thenReturn(true);
        when(reportesClient.obtenerReportesPorUsuario(10L))
                .thenThrow(new RuntimeException("Timeout"));

        // When
        List<ReporteResponseDTO> resultado = usuarioService.obtenerReportesPorUsuario(10L);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}