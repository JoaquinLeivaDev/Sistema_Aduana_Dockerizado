package cl.duocuc.aduana_api.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DocumentoResponseDTO {
    private Long id;
    private String tipo;
    private String urlArchivo;
    private boolean estadoValidacion;
    private Long idPasajero;
}