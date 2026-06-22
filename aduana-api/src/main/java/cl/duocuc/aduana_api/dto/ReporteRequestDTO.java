package cl.duocuc.aduana_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ReporteRequestDTO {

    @NotBlank(message = "El tipo de reporte es obligatorio")
    @Size(max = 50)
    private String tipo;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String datos;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long idUsuario;
}