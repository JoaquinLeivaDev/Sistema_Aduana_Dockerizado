package cl.duocuc.aduana_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TurnoRequestDTO {

    @NotNull(message = "El número de turno es obligatorio")
    @Min(value = 1, message = "El número de turno debe ser mayor a 0")
    private Integer numero;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "El id del pasajero es obligatorio")
    private Long idPasajero;
}