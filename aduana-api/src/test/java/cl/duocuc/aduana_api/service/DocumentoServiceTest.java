package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.DocumentoRequestDTO;
import cl.duocuc.aduana_api.dto.DocumentoResponseDTO;
import cl.duocuc.aduana_api.exception.DocumentoNotFoundException;
import cl.duocuc.aduana_api.exception.PasajeroNotFoundException;
import cl.duocuc.aduana_api.model.Documento;
import cl.duocuc.aduana_api.model.Pasajero;
import cl.duocuc.aduana_api.repository.DocumentoRepository;
import cl.duocuc.aduana_api.repository.PasajeroRepository;
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
class DocumentoServiceTest {

    @Mock
    private DocumentoRepository documentoRepository;

    @Mock
    private PasajeroRepository pasajeroRepository;

    @InjectMocks
    private DocumentoService documentoService;

    private Pasajero pasajero;
    private Documento documento;

    @BeforeEach
    void setUp() {
        pasajero = new Pasajero();
        pasajero.setIdPasajero(1L);

        documento = new Documento();
        documento.setId(1L);
        documento.setTipo("Pasaporte");
        documento.setUrlArchivo("http://archivo.com/doc.pdf");
        documento.setEstadoValidacion(false);
        documento.setPasajero(pasajero);
    }

    // ── obtenerTodos ──────────────────────────────────────────────────────────

    @Test
    void obtenerTodos_debeRetornarListaDeDocumentos() {
        when(documentoRepository.findAll()).thenReturn(List.of(documento));

        List<DocumentoResponseDTO> resultado = documentoService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("Pasaporte");
        verify(documentoRepository).findAll();
    }

    @Test
    void obtenerTodos_sinDocumentos_debeRetornarListaVacia() {
        when(documentoRepository.findAll()).thenReturn(List.of());

        List<DocumentoResponseDTO> resultado = documentoService.obtenerTodos();

        assertThat(resultado).isEmpty();
    }

    // ── buscarPorId ───────────────────────────────────────────────────────────

    @Test
    void buscarPorId_existente_debeRetornarDocumento() {
        when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));

        DocumentoResponseDTO resultado = documentoService.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTipo()).isEqualTo("Pasaporte");
        assertThat(resultado.getUrlArchivo()).isEqualTo("http://archivo.com/doc.pdf");
    }

    @Test
    void buscarPorId_noExistente_debeLanzarDocumentoNotFoundException() {
        when(documentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentoService.buscarPorId(99L))
                .isInstanceOf(DocumentoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── buscarPorPasajero ─────────────────────────────────────────────────────

    @Test
    void buscarPorPasajero_pasajeroExistente_debeRetornarDocumentos() {
        when(pasajeroRepository.existsById(1L)).thenReturn(true);
        when(documentoRepository.findByPasajeroIdPasajero(1L)).thenReturn(List.of(documento));

        List<DocumentoResponseDTO> resultado = documentoService.buscarPorPasajero(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdPasajero()).isEqualTo(1L);
    }

    @Test
    void buscarPorPasajero_pasajeroNoExiste_debeLanzarPasajeroNotFoundException() {
        when(pasajeroRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> documentoService.buscarPorPasajero(99L))
                .isInstanceOf(PasajeroNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── registrarDocumento ────────────────────────────────────────────────────

    @Test
    void registrarDocumento_pasajeroExistente_debeGuardarYRetornar() {
        DocumentoRequestDTO dto = new DocumentoRequestDTO();
        dto.setTipo("Visa");
        dto.setUrlArchivo("http://archivo.com/visa.pdf");
        dto.setIdPasajero(1L);

        when(pasajeroRepository.findById(1L)).thenReturn(Optional.of(pasajero));
        when(documentoRepository.save(any(Documento.class))).thenAnswer(inv -> {
            Documento d = inv.getArgument(0);
            d.setId(2L);
            return d;
        });

        DocumentoResponseDTO resultado = documentoService.registrarDocumento(dto);

        assertThat(resultado.getTipo()).isEqualTo("Visa");
        assertThat(resultado.isEstadoValidacion()).isFalse();
        assertThat(resultado.getIdPasajero()).isEqualTo(1L);
        verify(documentoRepository).save(any(Documento.class));
    }

    @Test
    void registrarDocumento_pasajeroNoExiste_debeLanzarPasajeroNotFoundException() {
        DocumentoRequestDTO dto = new DocumentoRequestDTO();
        dto.setTipo("Visa");
        dto.setIdPasajero(99L);

        when(pasajeroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentoService.registrarDocumento(dto))
                .isInstanceOf(PasajeroNotFoundException.class)
                .hasMessageContaining("99");

        verify(documentoRepository, never()).save(any());
    }

    // ── validarDocumento ──────────────────────────────────────────────────────

    @Test
    void validarDocumento_existente_debeMarcarComoValidado() {
        when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));
        when(documentoRepository.save(documento)).thenReturn(documento);

        DocumentoResponseDTO resultado = documentoService.validarDocumento(1L);

        assertThat(resultado.isEstadoValidacion()).isTrue();
        verify(documentoRepository).save(documento);
    }

    @Test
    void validarDocumento_noExistente_debeLanzarDocumentoNotFoundException() {
        when(documentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentoService.validarDocumento(99L))
                .isInstanceOf(DocumentoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── actualizarDocumento ───────────────────────────────────────────────────

    @Test
    void actualizarDocumento_existente_debeActualizarCampos() {
        DocumentoRequestDTO dto = new DocumentoRequestDTO();
        dto.setTipo("Cédula");
        dto.setUrlArchivo("http://archivo.com/cedula.pdf");

        when(documentoRepository.findById(1L)).thenReturn(Optional.of(documento));
        when(documentoRepository.save(documento)).thenReturn(documento);

        DocumentoResponseDTO resultado = documentoService.actualizarDocumento(1L, dto);

        assertThat(resultado.getTipo()).isEqualTo("Cédula");
        assertThat(resultado.getUrlArchivo()).isEqualTo("http://archivo.com/cedula.pdf");
    }

    @Test
    void actualizarDocumento_noExistente_debeLanzarDocumentoNotFoundException() {
        DocumentoRequestDTO dto = new DocumentoRequestDTO();
        dto.setTipo("Cédula");

        when(documentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentoService.actualizarDocumento(99L, dto))
                .isInstanceOf(DocumentoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── eliminarDocumento ─────────────────────────────────────────────────────

    @Test
    void eliminarDocumento_existente_debeEliminar() {
        when(documentoRepository.existsById(1L)).thenReturn(true);

        documentoService.eliminarDocumento(1L);

        verify(documentoRepository).deleteById(1L);
    }

    @Test
    void eliminarDocumento_noExistente_debeLanzarDocumentoNotFoundException() {
        when(documentoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> documentoService.eliminarDocumento(99L))
                .isInstanceOf(DocumentoNotFoundException.class)
                .hasMessageContaining("99");

        verify(documentoRepository, never()).deleteById(any());
    }
}
