package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.RolRequestDTO;
import cl.duocuc.aduana_api.dto.RolResponseDTO;
import cl.duocuc.aduana_api.exception.RolNotFoundException;
import cl.duocuc.aduana_api.model.Rol;
import cl.duocuc.aduana_api.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository repository;

    @InjectMocks
    private RolService rolService;

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ADMIN");
    }

    // ── obtenerTodos ──────────────────────────────────────────────────────────

    @Test
    void obtenerTodos_debeRetornarListaDeRoles() {
        when(repository.findAll()).thenReturn(List.of(rol));

        List<RolResponseDTO> resultado = rolService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("ADMIN");
        verify(repository).findAll();
    }

    @Test
    void obtenerTodos_sinRoles_debeRetornarListaVacia() {
        when(repository.findAll()).thenReturn(List.of());

        List<RolResponseDTO> resultado = rolService.obtenerTodos();

        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void buscarPorId_existente_debeRetornarRol() {
        when(repository.findById(1L)).thenReturn(Optional.of(rol));

        RolResponseDTO resultado = rolService.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("ADMIN");
    }

    @Test
    void buscarPorId_noExistente_debeLanzarRolNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rolService.buscarPorId(99L))
                .isInstanceOf(RolNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── registrarRol ──────────────────────────────────────────────────────────

    @Test
    void registrarRol_nombreNuevo_debeGuardarYRetornar() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("OPERADOR");

        Rol nuevoRol = new Rol();
        nuevoRol.setId(2L);
        nuevoRol.setNombre("OPERADOR");

        when(repository.existsByNombre("OPERADOR")).thenReturn(false);
        when(repository.save(any(Rol.class))).thenReturn(nuevoRol);

        RolResponseDTO resultado = rolService.registrarRol(dto);

        assertThat(resultado.getNombre()).isEqualTo("OPERADOR");
        assertThat(resultado.getId()).isEqualTo(2L);
        verify(repository).save(any(Rol.class));
    }

    @Test
    void registrarRol_nombreDuplicado_debeLanzarIllegalArgumentException() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("ADMIN");

        when(repository.existsByNombre("ADMIN")).thenReturn(true);

        assertThatThrownBy(() -> rolService.registrarRol(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ADMIN");

        verify(repository, never()).save(any());
    }

    // ── actualizarRol ─────────────────────────────────────────────────────────

    @Test
    void actualizarRol_existente_debeActualizarNombre() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("SUPERVISOR");

        when(repository.findById(1L)).thenReturn(Optional.of(rol));
        when(repository.save(rol)).thenReturn(rol);

        RolResponseDTO resultado = rolService.actualizarRol(1L, dto);

        assertThat(resultado.getNombre()).isEqualTo("SUPERVISOR");
        verify(repository).save(rol);
    }

    @Test
    void actualizarRol_noExistente_debeLanzarRolNotFoundException() {
        RolRequestDTO dto = new RolRequestDTO();
        dto.setNombre("SUPERVISOR");

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rolService.actualizarRol(99L, dto))
                .isInstanceOf(RolNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── eliminarRol ───────────────────────────────────────────────────────────

    @Test
    void eliminarRol_existente_debeEliminar() {
        when(repository.existsById(1L)).thenReturn(true);

        rolService.eliminarRol(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarRol_noExistente_debeLanzarRolNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> rolService.eliminarRol(99L))
                .isInstanceOf(RolNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).deleteById(any());
    }
}
