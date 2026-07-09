package cl.duocuc.aduana_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO utilizado para registrar un reporte")
public class ReporteRequestDTO {

    @Schema(description = "Tipo de reporte")
    @NotBlank(message = "El tipo de reporte es obligatorio")
    @Size(max = 50)
    private String tipo;

    @Schema(description = "Fecha asociada al reporte")
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Schema(description = "Datos o contenido adicional del reporte")
    private String datos;

    @Schema(description = "ID del usuario que genera el reporte")
    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;
}