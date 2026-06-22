package cl.duocuc.aduana_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class VehiculoRequestDTO {

    @NotBlank(message = "La patente es obligatoria")
    @Size(max = 10, message = "La patente no puede superar 10 caracteres")
    private String patente;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    private String tipo;

    @NotNull(message = "La fecha de admisión es obligatoria")
    private LocalDate fechaAdmision;

    // Opcional — asociar documento al registrar
    private Long idDocumento;
}