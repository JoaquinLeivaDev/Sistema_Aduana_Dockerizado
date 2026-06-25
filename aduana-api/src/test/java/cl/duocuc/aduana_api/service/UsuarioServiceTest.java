package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.UsuarioResponseDTO;
import cl.duocuc.aduana_api.model.Rol;
import cl.duocuc.aduana_api.model.Usuario;
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

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testBuscarUsuarioPorId_Existente() {
        // 1. Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        Rol rol = new Rol();
        rol.setNombre("ADMIN");
        usuario.setRol(rol);

        // 2. Act
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1L);

        // 3. Assert
        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUsername());
        assertEquals("ADMIN", resultado.getNombreRol());
        verify(usuarioRepository, times(1)).findById(1L);
    }
}