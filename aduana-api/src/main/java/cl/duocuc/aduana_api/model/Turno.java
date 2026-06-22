package cl.duocuc.aduana_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "turno")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turno_seq")
    @SequenceGenerator(name = "turno_seq", sequenceName = "TURNO_SEQ", allocationSize = 1)
    @Column(name = "id_turno")
    private Long id;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pasajero", nullable = false)
    private Pasajero pasajero;

    // Método del diagrama de clases
    public void asignarVentanilla(Integer numeroVentanilla) {
        this.estado = "Asignado-V" + numeroVentanilla;
    }
}