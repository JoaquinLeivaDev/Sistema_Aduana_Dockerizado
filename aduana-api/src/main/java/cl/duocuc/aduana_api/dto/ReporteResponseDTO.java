package cl.duocuc.aduana_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO utilizado para mostrar la información de un reporte")
public class ReporteResponseDTO {

    @Schema(description = "Identificador único del reporte")
    private Long idReporte;

    @Schema(description = "Tipo de reporte")
    private String tipo;

    @Schema(description = "Fecha asociada al reporte")
    private LocalDate fecha;

    @Schema(description = "Datos o contenido adicional del reporte")
    private String datos;

    @Schema(description = "ID del usuario asociado al reporte")
    private Long idUsuario;
}