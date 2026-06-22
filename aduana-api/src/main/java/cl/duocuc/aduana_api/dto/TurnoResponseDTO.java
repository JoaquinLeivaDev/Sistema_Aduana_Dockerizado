package cl.duocuc.aduana_api.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TurnoResponseDTO {
    private Long id;
    private Integer numero;
    private String estado;
    private Long idPasajero;
    private String nombrePasajero;
}