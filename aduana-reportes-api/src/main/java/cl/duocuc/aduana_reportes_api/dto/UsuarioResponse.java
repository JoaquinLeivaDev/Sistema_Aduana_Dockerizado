// UsuarioResponse.java
package cl.duocuc.aduana_reportes_api.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String username;
    private String nombreRol;
}