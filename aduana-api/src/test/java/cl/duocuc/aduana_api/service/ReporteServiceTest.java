package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.ReporteRequestDTO;
import cl.duocuc.aduana_api.dto.ReporteResponseDTO;
import cl.duocuc.aduana_api.exception.ReporteNotFoundException;
import cl.duocuc.aduana_api.exception.UsuarioNotFoundException;
import cl.duocuc.aduana_api.model.Reporte;
import cl.duocuc.aduana_api.model.Usuario;
import cl.duocuc.aduana_api.repository.ReporteRepository;
import cl.duocuc.aduana_api.repository.UsuarioRepository;
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
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Usuario usuario;
    private Reporte reporte;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");

        reporte = new Reporte();
        reporte.setIdReporte(1L);
        reporte.setTipo("INCIDENCIA");
        reporte.setFecha(LocalDate.of(2024, 1, 15));
        reporte.setDatos("Datos del reporte");
        reporte.setUsuario(usuario);
    }

    // ── obtenerTodos ──────────────────────────────────────────────────────────

    @Test
    void obtenerTodos_debeRetornarListaDeReportes() {
        when(reporteRepository.findAll()).thenReturn(List.of(reporte));

        List<ReporteResponseDTO> resultado = reporteService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("INCIDENCIA");
        verify(reporteRepository).findAll();
    }

    @Test
    void obtenerTodos_sinReportes_debeRetornarListaVacia() {
        when(reporteRepository.findAll()).thenReturn(List.of());

        List<ReporteResponseDTO> resultado = reporteService.obtenerTodos();

        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void buscarPorId_existente_debeRetornarReporte() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));

        ReporteResponseDTO resultado = reporteService.buscarPorId(1L);

        assertThat(resultado.getIdReporte()).isEqualTo(1L);
        assertThat(resultado.getTipo()).isEqualTo("INCIDENCIA");
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
    }

    @Test
    void buscarPorId_noExistente_debeLanzarReporteNotFoundException() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.buscarPorId(99L))
                .isInstanceOf(ReporteNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── obtenerPorUsuario ─────────────────────────────────────────────────────

    @Test
    void obtenerPorUsuario_debeRetornarReportesDelUsuario() {
        when(reporteRepository.findByUsuario_Id(1L)).thenReturn(List.of(reporte));

        List<ReporteResponseDTO> resultado = reporteService.obtenerPorUsuario(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdUsuario()).isEqualTo(1L);
    }

    @Test
    void obtenerPorUsuario_sinReportes_debeRetornarListaVacia() {
        when(reporteRepository.findByUsuario_Id(99L)).thenReturn(List.of());

        List<ReporteResponseDTO> resultado = reporteService.obtenerPorUsuario(99L);

        assertThat(resultado).isEmpty();
    }

    // ── obtenerPorTipo ────────────────────────────────────────────────────────

    @Test
    void obtenerPorTipo_debeRetornarReportesDelTipo() {
        when(reporteRepository.findByTipo("INCIDENCIA")).thenReturn(List.of(reporte));

        List<ReporteResponseDTO> resultado = reporteService.obtenerPorTipo("INCIDENCIA");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("INCIDENCIA");
    }

    @Test
    void obtenerPorTipo_tipoNoExiste_debeRetornarListaVacia() {
        when(reporteRepository.findByTipo("INEXISTENTE")).thenReturn(List.of());

        List<ReporteResponseDTO> resultado = reporteService.obtenerPorTipo("INEXISTENTE");

        assertThat(resultado).isEmpty();
    }

    // ── registrar ─────────────────────────────────────────────────────────────

    @Test
    void registrar_usuarioExistente_debeGuardarYRetornar() {
        ReporteRequestDTO dto = new ReporteRequestDTO();
        dto.setTipo("CONTROL");
        dto.setFecha(LocalDate.of(2024, 6, 1));
        dto.setDatos("Datos control");
        dto.setIdUsuario(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(inv -> {
            Reporte r = inv.getArgument(0);
            r.setIdReporte(2L);
            return r;
        });

        ReporteResponseDTO resultado = reporteService.registrar(dto);

        assertThat(resultado.getTipo()).isEqualTo("CONTROL");
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        verify(reporteRepository).save(any(Reporte.class));
    }

    @Test
    void registrar_usuarioNoExiste_debeLanzarUsuarioNotFoundException() {
        ReporteRequestDTO dto = new ReporteRequestDTO();
        dto.setTipo("CONTROL");
        dto.setIdUsuario(99L);

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.registrar(dto))
                .isInstanceOf(UsuarioNotFoundException.class)
                .hasMessageContaining("99");

        verify(reporteRepository, never()).save(any());
    }

    // ── eliminar ──────────────────────────────────────────────────────────────

    @Test
    void eliminar_existente_debeEliminar() {
        when(reporteRepository.existsById(1L)).thenReturn(true);

        reporteService.eliminar(1L);

        verify(reporteRepository).deleteById(1L);
    }

    @Test
    void eliminar_noExistente_debeLanzarReporteNotFoundException() {
        when(reporteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> reporteService.eliminar(99L))
                .isInstanceOf(ReporteNotFoundException.class)
                .hasMessageContaining("99");

        verify(reporteRepository, never()).deleteById(any());
    }
}
