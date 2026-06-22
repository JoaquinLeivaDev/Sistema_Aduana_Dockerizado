// UsuarioResponseDTO.java
package cl.duocuc.aduana_api.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String nombreRol;
    // Sin passwordHash — nunca exponemos la contraseña en la respuesta
}