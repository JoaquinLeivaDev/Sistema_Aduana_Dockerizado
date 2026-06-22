package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.PasajeroRequestDTO;
import cl.duocuc.aduana_api.dto.PasajeroResponseDTO;
import cl.duocuc.aduana_api.exception.PasajeroNotFoundException;
import cl.duocuc.aduana_api.model.Pasajero;
import cl.duocuc.aduana_api.repository.PasajeroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasajeroServiceTest {

    @Mock
    private PasajeroRepository repository;

    @InjectMocks
    private PasajeroService pasajeroService;

    private Pasajero pasajero;

    @BeforeEach
    void setUp() {
        pasajero = new Pasajero();
        pasajero.setIdPasajero(1L);
        pasajero.setRut("12345678-9");
        pasajero.setNombre("Juan");
        pasajero.setApellidos("Pérez");
        pasajero.setFechaNac(LocalDate.of(1990, 5, 15));
        pasajero.setCorreo("juan@example.com");
    }

    // ── obtenerTodos ──────────────────────────────────────────────────────────

    @Test
    void obtenerTodos_debeRetornarListaDePasajeros() {
        when(repository.findAll()).thenReturn(List.of(pasajero));

        List<PasajeroResponseDTO> resultado = pasajeroService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        verify(repository).findAll();
    }

    @Test
    void obtenerTodos_sinPasajeros_debeRetornarListaVacia() {
        when(repository.findAll()).thenReturn(List.of());

        List<PasajeroResponseDTO> resultado = pasajeroService.obtenerTodos();

        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void buscarPorId_existente_debeRetornarPasajero() {
        when(repository.findById(1L)).thenReturn(Optional.of(pasajero));

        PasajeroResponseDTO resultado = pasajeroService.buscarPorId(1L);

        assertThat(resultado.getIdPasajero()).isEqualTo(1L);
        assertThat(resultado.getRut()).isEqualTo("12345678-9");
        assertThat(resultado.getNombre()).isEqualTo("Juan");
    }

    @Test
    void buscarPorId_noExistente_debeLanzarPasajeroNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pasajeroService.buscarPorId(99L))
                .isInstanceOf(PasajeroNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── buscarPorRut ──────────────────────────────────────────────────────────

    @Test
    void buscarPorRut_existente_debeRetornarPasajero() {
        when(repository.findByRut("12345678-9")).thenReturn(pasajero);

        PasajeroResponseDTO resultado = pasajeroService.buscarPorRut("12345678-9");

        assertThat(resultado.getRut()).isEqualTo("12345678-9");
        assertThat(resultado.getNombre()).isEqualTo("Juan");
    }

    @Test
    void buscarPorRut_noExistente_debeLanzarPasajeroNotFoundException() {
        when(repository.findByRut("99999999-9")).thenReturn(null);

        assertThatThrownBy(() -> pasajeroService.buscarPorRut("99999999-9"))
                .isInstanceOf(PasajeroNotFoundException.class)
                .hasMessageContaining("99999999-9");
    }

    // ── registrarPasajero ─────────────────────────────────────────────────────

    @Test
    void registrarPasajero_rutNuevo_debeGuardarYRetornar() {
        PasajeroRequestDTO dto = new PasajeroRequestDTO();
        dto.setRut("98765432-1");
        dto.setNombre("María");
        dto.setApellidos("González");
        dto.setFechaNac(LocalDate.of(1995, 3, 20));
        dto.setCorreo("maria@example.com");

        Pasajero nuevo = new Pasajero();
        nuevo.setIdPasajero(2L);
        nuevo.setRut("98765432-1");
        nuevo.setNombre("María");
        nuevo.setApellidos("González");
        nuevo.setFechaNac(LocalDate.of(1995, 3, 20)); // Aseguramos fecha no nula

        when(repository.existsByRut("98765432-1")).thenReturn(false);
        when(repository.save(any(Pasajero.class))).thenReturn(nuevo);

        PasajeroResponseDTO resultado = pasajeroService.registrarPasajero(dto);

        assertThat(resultado.getNombre()).isEqualTo("María");
        assertThat(resultado.getRut()).isEqualTo("98765432-1");
        verify(repository).save(any(Pasajero.class));
    }

    @Test
    void registrarPasajero_rutDuplicado_debeLanzarIllegalArgumentException() {
        PasajeroRequestDTO dto = new PasajeroRequestDTO();
        dto.setRut("12345678-9");

        when(repository.existsByRut("12345678-9")).thenReturn(true);

        assertThatThrownBy(() -> pasajeroService.registrarPasajero(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("12345678-9");

        verify(repository, never()).save(any());
    }

    // ── actualizarPasajero ────────────────────────────────────────────────────

    @Test
    void actualizarPasajero_existente_debeActualizarCampos() {
        PasajeroRequestDTO dto = new PasajeroRequestDTO();
        dto.setNombre("Pedro");
        dto.setApellidos("López");
        dto.setFechaNac(LocalDate.of(1988, 7, 10));
        dto.setCorreo("pedro@example.com");

        when(repository.findById(1L)).thenReturn(Optional.of(pasajero));
        when(repository.save(pasajero)).thenReturn(pasajero);

        PasajeroResponseDTO resultado = pasajeroService.actualizarPasajero(1L, dto);

        assertThat(resultado.getNombre()).isEqualTo("Pedro");
        assertThat(resultado.getApellidos()).isEqualTo("López");
        verify(repository).save(pasajero);
    }

    @Test
    void actualizarPasajero_noExistente_debeLanzarPasajeroNotFoundException() {
        PasajeroRequestDTO dto = new PasajeroRequestDTO();
        dto.setNombre("Pedro");

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pasajeroService.actualizarPasajero(99L, dto))
                .isInstanceOf(PasajeroNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── eliminarPasajero ──────────────────────────────────────────────────────

    @Test
    void eliminarPasajero_existente_debeEliminar() {
        when(repository.existsById(1L)).thenReturn(true);

        pasajeroService.eliminarPasajero(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarPasajero_noExistente_debeLanzarPasajeroNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> pasajeroService.eliminarPasajero(99L))
                .isInstanceOf(PasajeroNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).deleteById(any());
    }
}
