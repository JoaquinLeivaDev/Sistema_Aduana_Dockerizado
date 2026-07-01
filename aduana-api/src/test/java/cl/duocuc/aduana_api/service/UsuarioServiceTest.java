package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.UsuarioResponseDTO;
import cl.duocuc.aduana_api.exception.UsuarioNotFoundException;
import cl.duocuc.aduana_api.model.Rol;
import cl.duocuc.aduana_api.model.Usuario;
import cl.duocuc.aduana_api.repository.ReporteRepository;
import cl.duocuc.aduana_api.repository.RolRepository;
import cl.duocuc.aduana_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private ReporteRepository reporteRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testBuscarUsuarioPorId_Existente() {
        // Arrange: preparar usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        Rol rol = new Rol();
        rol.setNombre("ADMIN");
        usuario.setRol(rol);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        // Act: ejecutar el servicio
        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1L);

        // Assert: comprobar resultado
        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUsername());
        assertEquals("ADMIN", resultado.getNombreRol());

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarUsuarioPorId_NoExistente() {
        // Arrange: el repositorio falso no encuentra usuario
        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act y Assert: debe lanzar la excepción esperada
        UsuarioNotFoundException exception = assertThrows(
                UsuarioNotFoundException.class,
                () -> usuarioService.buscarPorId(99L)
        );

        assertEquals(
                "Usuario con id 99 no encontrado",
                exception.getMessage()
        );

        verify(usuarioRepository, times(1)).findById(99L);
    }
}