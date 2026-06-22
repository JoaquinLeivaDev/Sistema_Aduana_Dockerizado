// PasajeroResponse.java
package cl.duocuc.aduana_reportes_api.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PasajeroResponse {
    private Long idPasajero;
    private String rut;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNac;
    private String correo;
    private int edad;
}