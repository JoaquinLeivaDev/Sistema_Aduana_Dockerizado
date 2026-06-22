package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.TurnoRequestDTO;
import cl.duocuc.aduana_api.dto.TurnoResponseDTO;
import cl.duocuc.aduana_api.exception.PasajeroNotFoundException;
import cl.duocuc.aduana_api.exception.TurnoNotFoundException;
import cl.duocuc.aduana_api.model.Pasajero;
import cl.duocuc.aduana_api.model.Turno;
import cl.duocuc.aduana_api.repository.PasajeroRepository;
import cl.duocuc.aduana_api.repository.TurnoRepository;
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
class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private PasajeroRepository pasajeroRepository;

    @InjectMocks
    private TurnoService turnoService;

    private Pasajero pasajero;
    private Turno turno;

    @BeforeEach
    void setUp() {
        pasajero = new Pasajero();
        pasajero.setIdPasajero(1L);
        pasajero.setNombre("Juan");

        turno = new Turno();
        turno.setId(1L);
        turno.setNumero(101);
        turno.setEstado("ESPERA");
        turno.setPasajero(pasajero);
    }

    // ── obtenerTodos ──────────────────────────────────────────────────────────

    @Test
    void obtenerTodos_debeRetornarListaDeTurnos() {
        when(turnoRepository.findAll()).thenReturn(List.of(turno));

        List<TurnoResponseDTO> resultado = turnoService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNumero()).isEqualTo(101);
        verify(turnoRepository).findAll();
    }

    @Test
    void obtenerTodos_sinTurnos_debeRetornarListaVacia() {
        when(turnoRepository.findAll()).thenReturn(List.of());

        List<TurnoResponseDTO> resultado = turnoService.obtenerTodos();

        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void buscarPorId_existente_debeRetornarTurno() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));

        TurnoResponseDTO resultado = turnoService.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("ESPERA");
        assertThat(resultado.getNombrePasajero()).isEqualTo("Juan");
    }

    @Test
    void buscarPorId_noExistente_debeLanzarTurnoNotFoundException() {
        when(turnoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> turnoService.buscarPorId(99L))
                .isInstanceOf(TurnoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── buscarPorPasajero ─────────────────────────────────────────────────────

    @Test
    void buscarPorPasajero_existente_debeRetornarTurno() {
        when(turnoRepository.findByPasajeroIdPasajero(1L)).thenReturn(turno);

        TurnoResponseDTO resultado = turnoService.buscarPorPasajero(1L);

        assertThat(resultado.getIdPasajero()).isEqualTo(1L);
        assertThat(resultado.getNumero()).isEqualTo(101);
    }

    @Test
    void buscarPorPasajero_sinTurno_debeLanzarTurnoNotFoundException() {
        when(turnoRepository.findByPasajeroIdPasajero(99L)).thenReturn(null);

        assertThatThrownBy(() -> turnoService.buscarPorPasajero(99L))
                .isInstanceOf(TurnoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── registrarTurno ────────────────────────────────────────────────────────

    @Test
    void registrarTurno_pasajeroSinTurno_debeGuardarYRetornar() {
        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setNumero(202);
        dto.setEstado("ESPERA");
        dto.setIdPasajero(1L);

        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));
        when(turnoRepository.existsByPasajeroIdPasajero(1L)).thenReturn(false);
        when(turnoRepository.save(any(Turno.class))).thenAnswer(inv -> {
            Turno t = inv.getArgument(0);
            t.setId(2L);
            return t;
        });

        TurnoResponseDTO resultado = turnoService.registrarTurno(dto);

        assertThat(resultado.getNumero()).isEqualTo(202);
        assertThat(resultado.getEstado()).isEqualTo("ESPERA");
        verify(turnoRepository).save(any(Turno.class));
    }

    @Test
    void registrarTurno_pasajeroNoExiste_debeLanzarPasajeroNotFoundException() {
        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setIdPasajero(99L);

        when(pasajeroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> turnoService.registrarTurno(dto))
                .isInstanceOf(PasajeroNotFoundException.class)
                .hasMessageContaining("99");

        verify(turnoRepository, never()).save(any());
    }

    @Test
    void registrarTurno_pasajeroYaTieneTurno_debeLanzarIllegalArgumentException() {
        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setIdPasajero(1L);

        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));
        when(turnoRepository.existsByPasajeroIdPasajero(1L)).thenReturn(true);

        assertThatThrownBy(() -> turnoService.registrarTurno(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya tiene un turno");

        verify(turnoRepository, never()).save(any());
    }

    // ── asignarVentanilla (regla de negocio) ──────────────────────────────────

    @Test
    void asignarVentanilla_turnoExistente_debeAsignarYRetornar() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(turno)).thenReturn(turno);

        // asignarVentanilla delega en turno.asignarVentanilla(n), se asume que el modelo lo maneja
        TurnoResponseDTO resultado = turnoService.asignarVentanilla(1L, 3);

        assertThat(resultado).isNotNull();
        verify(turnoRepository).save(turno);
    }

    @Test
    void asignarVentanilla_turnoNoExistente_debeLanzarTurnoNotFoundException() {
        when(turnoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> turnoService.asignarVentanilla(99L, 3))
                .isInstanceOf(TurnoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── actualizarEstado ──────────────────────────────────────────────────────

    @Test
    void actualizarEstado_turnoExistente_debeActualizarEstado() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(turno)).thenReturn(turno);

        TurnoResponseDTO resultado = turnoService.actualizarEstado(1L, "ATENDIDO");

        assertThat(resultado.getEstado()).isEqualTo("ATENDIDO");
        verify(turnoRepository).save(turno);
    }

    @Test
    void actualizarEstado_turnoNoExistente_debeLanzarTurnoNotFoundException() {
        when(turnoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> turnoService.actualizarEstado(99L, "ATENDIDO"))
                .isInstanceOf(TurnoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── eliminarTurno ─────────────────────────────────────────────────────────

    @Test
    void eliminarTurno_existente_debeEliminar() {
        when(turnoRepository.existsById(1L)).thenReturn(true);

        turnoService.eliminarTurno(1L);

        verify(turnoRepository).deleteById(1L);
    }

    @Test
    void eliminarTurno_noExistente_debeLanzarTurnoNotFoundException() {
        when(turnoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> turnoService.eliminarTurno(99L))
                .isInstanceOf(TurnoNotFoundException.class)
                .hasMessageContaining("99");

        verify(turnoRepository, never()).deleteById(any());
    }
}
