package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.VehiculoRequestDTO;
import cl.duocuc.aduana_api.dto.VehiculoResponseDTO;
import cl.duocuc.aduana_api.exception.DocumentoNotFoundException;
import cl.duocuc.aduana_api.exception.VehiculoNotFoundException;
import cl.duocuc.aduana_api.model.Documento;
import cl.duocuc.aduana_api.model.Vehiculo;
import cl.duocuc.aduana_api.repository.DocumentoRepository;
import cl.duocuc.aduana_api.repository.VehiculoRepository;
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
class VehiculoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private DocumentoRepository documentoRepository;

    @InjectMocks
    private VehiculoService vehiculoService;

    private Vehiculo vehiculo;
    private Documento documento;

    @BeforeEach
    void setUp() {
        documento = new Documento();
        documento.setId(1L);
        documento.setTipo("Permiso de circulación");
        documento.setEstadoValidacion(true);

        vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setPatente("ABCD12");
        vehiculo.setTipo("Automóvil");
        vehiculo.setFechaAdmision(LocalDate.of(2024, 3, 10));
        vehiculo.setDocumento(documento);
    }

    // ── obtenerTodos ──────────────────────────────────────────────────────────

    @Test
    void obtenerTodos_debeRetornarListaDeVehiculos() {
        when(vehiculoRepository.findAll()).thenReturn(List.of(vehiculo));

        List<VehiculoResponseDTO> resultado = vehiculoService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPatente()).isEqualTo("ABCD12");
        verify(vehiculoRepository).findAll();
    }

    @Test
    void obtenerTodos_sinVehiculos_debeRetornarListaVacia() {
        when(vehiculoRepository.findAll()).thenReturn(List.of());

        List<VehiculoResponseDTO> resultado = vehiculoService.obtenerTodos();

        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void buscarPorId_existente_debeRetornarVehiculo() {
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        VehiculoResponseDTO resultado = vehiculoService.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getPatente()).isEqualTo("ABCD12");
        assertThat(resultado.getTipo()).isEqualTo("Automóvil");
    }

    @Test
    void buscarPorId_noExistente_debeLanzarVehiculoNotFoundException() {
        when(vehiculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculoService.buscarPorId(99L))
                .isInstanceOf(VehiculoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── buscarPorPatente ──────────────────────────────────────────────────────

    @Test
    void buscarPorPatente_existente_debeRetornarVehiculo() {
        when(vehiculoRepository.findByPatente("ABCD12")).thenReturn(vehiculo);

        VehiculoResponseDTO resultado = vehiculoService.buscarPorPatente("ABCD12");

        assertThat(resultado.getPatente()).isEqualTo("ABCD12");
    }

    @Test
    void buscarPorPatente_noExistente_debeLanzarVehiculoNotFoundException() {
        when(vehiculoRepository.findByPatente("ZZZZ99")).thenReturn(null);

        assertThatThrownBy(() -> vehiculoService.buscarPorPatente("ZZZZ99"))
                .isInstanceOf(VehiculoNotFoundException.class)
                .hasMessageContaining("ZZZZ99");
    }

    // ── registrarVehiculo ─────────────────────────────────────────────────────

    @Test
    void registrarVehiculo_patenteNuevaSinDocumento_debeGuardarYRetornar() {
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setPatente("EFGH34");
        dto.setTipo("Camioneta");
        dto.setFechaAdmision(LocalDate.of(2024, 5, 1));
        dto.setIdDocumento(null);

        Vehiculo nuevo = new Vehiculo();
        nuevo.setId(2L);
        nuevo.setPatente("EFGH34");
        nuevo.setTipo("Camioneta");

        when(vehiculoRepository.existsByPatente("EFGH34")).thenReturn(false);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(nuevo);

        VehiculoResponseDTO resultado = vehiculoService.registrarVehiculo(dto);

        assertThat(resultado.getPatente()).isEqualTo("EFGH34");
        verify(vehiculoRepository).save(any(Vehiculo.class));
        verify(documentoRepository, never()).findById(any());
    }

    @Test
    void registrarVehiculo_patenteNuevaConDocumento_debeAsociarDocumento() {
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setPatente("EFGH34");
        dto.setTipo("Camioneta");
        dto.setFechaAdmision(LocalDate.of(2024, 5, 1));
        dto.setIdDocumento(1L);

        Vehiculo nuevo = new Vehiculo();
        nuevo.setId(2L);
        nuevo.setPatente("EFGH34");
        nuevo.setDocumento(documento);

        when(vehiculoRepository.existsByPatente("EFGH34")).thenReturn(false);
        when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(nuevo);

        VehiculoResponseDTO resultado = vehiculoService.registrarVehiculo(dto);

        assertThat(resultado.getIdDocumento()).isEqualTo(1L);
        verify(documentoRepository).findById(1L);
    }

    @Test
    void registrarVehiculo_patenteDuplicada_debeLanzarIllegalArgumentException() {
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setPatente("ABCD12");

        when(vehiculoRepository.existsByPatente("ABCD12")).thenReturn(true);

        assertThatThrownBy(() -> vehiculoService.registrarVehiculo(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ABCD12");

        verify(vehiculoRepository, never()).save(any());
    }

    @Test
    void registrarVehiculo_documentoNoExiste_debeLanzarDocumentoNotFoundException() {
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setPatente("EFGH34");
        dto.setIdDocumento(99L);

        when(vehiculoRepository.existsByPatente("EFGH34")).thenReturn(false);
        when(documentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculoService.registrarVehiculo(dto))
                .isInstanceOf(DocumentoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── generarSATVA (regla de negocio clave) ─────────────────────────────────

    @Test
    void generarSATVA_vehiculoConDocumentoValidado_debeRetornarVehiculo() {
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        VehiculoResponseDTO resultado = vehiculoService.generarSATVA(1L);

        assertThat(resultado.getPatente()).isEqualTo("ABCD12");
        assertThat(resultado.getIdDocumento()).isEqualTo(1L);
    }

    @Test
    void generarSATVA_vehiculoSinDocumento_debeLanzarIllegalArgumentException() {
        vehiculo.setDocumento(null);
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        assertThatThrownBy(() -> vehiculoService.generarSATVA(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("documento");
    }

    @Test
    void generarSATVA_documentoNoValidado_debeLanzarIllegalArgumentException() {
        documento.setEstadoValidacion(false);
        vehiculo.setDocumento(documento);
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        assertThatThrownBy(() -> vehiculoService.generarSATVA(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("validado");
    }

    @Test
    void generarSATVA_vehiculoNoExistente_debeLanzarVehiculoNotFoundException() {
        when(vehiculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculoService.generarSATVA(99L))
                .isInstanceOf(VehiculoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── actualizarVehiculo ────────────────────────────────────────────────────

    @Test
    void actualizarVehiculo_existente_debeActualizarCampos() {
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setTipo("Furgón");
        dto.setFechaAdmision(LocalDate.of(2024, 8, 20));

        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(vehiculoRepository.save(vehiculo)).thenReturn(vehiculo);

        VehiculoResponseDTO resultado = vehiculoService.actualizarVehiculo(1L, dto);

        assertThat(resultado.getTipo()).isEqualTo("Furgón");
        verify(vehiculoRepository).save(vehiculo);
    }

    @Test
    void actualizarVehiculo_noExistente_debeLanzarVehiculoNotFoundException() {
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setTipo("Furgón");

        when(vehiculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehiculoService.actualizarVehiculo(99L, dto))
                .isInstanceOf(VehiculoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── eliminarVehiculo ──────────────────────────────────────────────────────

    @Test
    void eliminarVehiculo_existente_debeEliminar() {
        when(vehiculoRepository.existsById(1L)).thenReturn(true);

        vehiculoService.eliminarVehiculo(1L);

        verify(vehiculoRepository).deleteById(1L);
    }

    @Test
    void eliminarVehiculo_noExistente_debeLanzarVehiculoNotFoundException() {
        when(vehiculoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> vehiculoService.eliminarVehiculo(99L))
                .isInstanceOf(VehiculoNotFoundException.class)
                .hasMessageContaining("99");

        verify(vehiculoRepository, never()).deleteById(any());
    }
}
