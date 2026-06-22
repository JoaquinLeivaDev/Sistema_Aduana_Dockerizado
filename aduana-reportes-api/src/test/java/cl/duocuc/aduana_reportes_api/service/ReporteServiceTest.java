package cl.duocuc.aduana_reportes_api.service;

import cl.duocuc.aduana_reportes_api.client.AduanaClient;
import cl.duocuc.aduana_reportes_api.dto.ApiResponse;
import cl.duocuc.aduana_reportes_api.dto.ReporteResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private AduanaClient aduanaClient;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    void validarId_ShouldThrowException_WhenIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> reporteService.validarId(0L));
        assertThrows(IllegalArgumentException.class, () -> reporteService.validarId(null));
    }

    @Test
    void validarId_ShouldNotThrowException_WhenIdIsValid() {
        assertDoesNotThrow(() -> reporteService.validarId(1L));
    }

    @Test
    void validarTipo_ShouldThrowException_WhenTipoIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> reporteService.validarTipo(""));
        assertThrows(IllegalArgumentException.class, () -> reporteService.validarTipo("a".repeat(51)));
    }

    @Test
    void validarFecha_ShouldThrowException_WhenFechaIsFuture() {
        assertThrows(IllegalArgumentException.class, () -> reporteService.validarFecha(LocalDate.now().plusDays(1)));
    }

    @Test
    void obtenerTodos_ShouldReturnReportes() {
        when(aduanaClient.obtenerReportes()).thenReturn(ApiResponse.ok(Collections.emptyList(), "OK"));
        
        ApiResponse<List<ReporteResponseDTO>> response = reporteService.obtenerTodos();
        
        assertNotNull(response);
        verify(aduanaClient, times(1)).obtenerReportes();
    }
}
