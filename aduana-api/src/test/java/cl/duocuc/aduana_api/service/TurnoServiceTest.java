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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void obtenerTodos_debeRetornarListaDeTurnos() {
        when(turnoRepository.findAll()).thenReturn(List.of(turno));

        List<TurnoResponseDTO> resultado = turnoService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
        assertThat(resultado.get(0).getNumero()).isEqualTo(101);
        assertThat(resultado.get(0).getNombrePasajero()).isEqualTo("Juan");
        verify(turnoRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarTurno() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));

        TurnoResponseDTO resultado = turnoService.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getEstado()).isEqualTo("ESPERA");
        verify(turnoRepository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarTurnoNotFoundException() {
        Long idInexistente = 99L;
        when(turnoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> turnoService.buscarPorId(idInexistente))
                .isInstanceOf(TurnoNotFoundException.class)
                .hasMessageContaining("99");

        verify(turnoRepository).findById(idInexistente);
    }

    @Test
    void registrarTurno_cuandoPasajeroExisteYNoTieneTurno_debeGuardar() {
        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setNumero(202);
        dto.setEstado("ESPERA");
        dto.setIdPasajero(1L);

        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));
        when(turnoRepository.existsByPasajeroIdPasajero(1L)).thenReturn(false);
        when(turnoRepository.save(any(Turno.class))).thenAnswer(invocation -> {
            Turno turnoGuardado = invocation.getArgument(0);
            turnoGuardado.setId(2L);
            return turnoGuardado;
        });

        TurnoResponseDTO resultado = turnoService.registrarTurno(dto);

        assertThat(resultado.getId()).isEqualTo(2L);
        assertThat(resultado.getNumero()).isEqualTo(202);
        assertThat(resultado.getIdPasajero()).isEqualTo(1L);
        verify(turnoRepository).save(any(Turno.class));
    }

    @Test
    void registrarTurno_cuandoPasajeroNoExiste_debeLanzarPasajeroNotFoundException() {
        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setNumero(202);
        dto.setEstado("ESPERA");
        dto.setIdPasajero(99L);

        when(pasajeroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> turnoService.registrarTurno(dto))
                .isInstanceOf(PasajeroNotFoundException.class)
                .hasMessageContaining("99");

        verify(turnoRepository, never()).save(any(Turno.class));
    }

    @Test
    void registrarTurno_cuandoPasajeroYaTieneTurno_debeLanzarIllegalArgumentException() {
        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setNumero(202);
        dto.setEstado("ESPERA");
        dto.setIdPasajero(1L);

        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));
        when(turnoRepository.existsByPasajeroIdPasajero(1L)).thenReturn(true);

        assertThatThrownBy(() -> turnoService.registrarTurno(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya tiene un turno");

        verify(turnoRepository, never()).save(any(Turno.class));
    }

    @Test
    void asignarVentanilla_cuandoTurnoExiste_debeActualizarEstadoYGuardar() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(turno)).thenReturn(turno);

        TurnoResponseDTO resultado = turnoService.asignarVentanilla(1L, 3);

        assertThat(resultado.getEstado()).isEqualTo("Asignado-V3");
        verify(turnoRepository).save(turno);
    }

    @Test
    void actualizarEstado_cuandoTurnoExiste_debeCambiarEstado() {
        when(turnoRepository.findById(1L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(turno)).thenReturn(turno);

        TurnoResponseDTO resultado = turnoService.actualizarEstado(1L, "ATENDIDO");

        assertThat(resultado.getEstado()).isEqualTo("ATENDIDO");
        verify(turnoRepository).save(turno);
    }

    @Test
    void eliminarTurno_cuandoExiste_debeEliminarPorId() {
        when(turnoRepository.existsById(1L)).thenReturn(true);

        turnoService.eliminarTurno(1L);

        verify(turnoRepository).deleteById(1L);
    }

    @Test
    void eliminarTurno_cuandoNoExiste_debeLanzarTurnoNotFoundException() {
        when(turnoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> turnoService.eliminarTurno(99L))
                .isInstanceOf(TurnoNotFoundException.class)
                .hasMessageContaining("99");

        verify(turnoRepository, never()).deleteById(any());
    }
}
