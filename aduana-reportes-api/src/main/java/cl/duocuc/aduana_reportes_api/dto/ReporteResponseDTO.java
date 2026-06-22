// ReporteResponseDTO.java
package cl.duocuc.aduana_reportes_api.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ReporteResponseDTO {
    private Long idReporte;
    private String tipo;
    private LocalDate fecha;
    private String datos;
    private Long idUsuario;
}