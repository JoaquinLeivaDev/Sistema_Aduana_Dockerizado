package cl.duocuc.aduana_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RolRequestDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 30, message = "El nombre no puede superar 30 caracteres")
    private String nombre;
}