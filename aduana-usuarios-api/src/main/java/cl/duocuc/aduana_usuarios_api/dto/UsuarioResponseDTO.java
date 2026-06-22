package cl.duocuc.aduana_usuarios_api.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String rolNombre;
}
