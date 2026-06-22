package cl.duocuc.aduana_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DocumentoRequestDTO {

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar 50 caracteres")
    private String tipo;

    @NotBlank(message = "La URL del archivo es obligatoria")
    private String urlArchivo;

    @NotNull(message = "El id del pasajero es obligatorio")
    private Long idPasajero;
}