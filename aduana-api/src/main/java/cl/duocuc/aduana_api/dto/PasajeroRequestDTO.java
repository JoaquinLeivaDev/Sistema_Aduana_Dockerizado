package cl.duocuc.aduana_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PasajeroRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "\\d{7,8}-[\\dkK]", message = "Formato de RUT inválido")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @Past(message = "La fecha de nacimiento debe ser pasada")
    @NotNull
    private LocalDate fechaNac;

    @Email(message = "Correo inválido")
    private String correo;
}