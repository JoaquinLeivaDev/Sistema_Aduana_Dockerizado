package cl.duocuc.aduana_usuarios_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RolRequestDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    @jakarta.validation.constraints.Size(max = 30)
    private String nombre;
}
