package cl.duocuc.aduana_usuarios_api.service;

import cl.duocuc.aduana_usuarios_api.dto.RolRequestDTO;
import cl.duocuc.aduana_usuarios_api.dto.RolResponseDTO;
import cl.duocuc.aduana_usuarios_api.exception.RolNotFoundException;
import cl.duocuc.aduana_usuarios_api.model.Rol;
import cl.duocuc.aduana_usuarios_api.repository.RolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    // ─── obtenerTodos ────────────────────────────────────────────────────────

    @Test
    void obtenerTodos_ShouldReturnListOfRoles() {
        // Given
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");
        when(rolRepository.findAll()).thenReturn(List.of(rol));

        // When
        List<RolResponseDTO> resultado = rolService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ADMIN", resultado.get(0).getNombre());
        verify(rolRepository, times(1)).findAll();
    }

    // ─── buscarPorId ─────────────────────────────────────────────────────────

    @Test
    void buscarPorId_ShouldReturnRol_WhenExists() {
        // Given
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("USER");
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        // When
        RolResponseDTO resultado = rolService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("USER", resultado.getNombre());
    }

    @Test
    void buscarPorId_ShouldThrowException_WhenNotFound() {
        // Given
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RolNotFoundException.class, () -> rolService.buscarPorId(99L));
        verify(rolRepository, times(1)).findById(99L);
    }

    // ─── crear ────────────────────────────────────────────────────────────────

    @Test
    void crear_ShouldCreateRol_WhenNombreIsUnique() {
        // Given
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("MODERADOR");

        Rol rolGuardado = new Rol();
        rolGuardado.setId(10L);
        rolGuardado.setNombre("MODERADOR");

        when(rolRepository.existsByNombre("MODERADOR")).thenReturn(false);
        when(rolRepository.save(any(Rol.class))).thenReturn(rolGuardado);

        // When
        RolResponseDTO resultado = rolService.crear(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("MODERADOR", resultado.getNombre());
        verify(rolRepository).save(any(Rol.class));
    }

    @Test
    void crear_ShouldThrowException_WhenNombreAlreadyExists() {
        // Given
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("ADMIN");

        when(rolRepository.existsByNombre("ADMIN")).thenReturn(true);

        // When & Then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> rolService.crear(dto)
        );
        assertTrue(ex.getMessage().contains("ADMIN"));
        verify(rolRepository, never()).save(any());
    }
}