// VehiculoResponseDTO.java
package cl.duocuc.aduana_api.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class VehiculoResponseDTO {
    private Long id;
    private String patente;
    private String tipo;
    private LocalDate fechaAdmision;
    private boolean satvaGenerado;
    private Long idDocumento;
}