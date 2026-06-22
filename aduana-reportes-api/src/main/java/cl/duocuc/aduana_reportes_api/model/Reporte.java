package cl.duocuc.aduana_reportes_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reporte {
    private Long id;
    private String tipo;
    private LocalDate fecha;
    private Long idUsuario;
}
