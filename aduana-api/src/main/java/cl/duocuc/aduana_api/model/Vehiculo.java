package cl.duocuc.aduana_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehiculo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehiculo_seq")
    @SequenceGenerator(name = "vehiculo_seq", sequenceName = "VEHICULO_SEQ", allocationSize = 1)
    @Column(name = "id_vehiculo")
    private Long id;

    @Column(name = "patente", nullable = false, length = 10, unique = true)
    private String patente;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "fecha_admision", nullable = false)
    private LocalDate fechaAdmision;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_documento", unique = true)
    private Documento documento;

    public boolean generarSATVA() {
        return this.documento != null && this.documento.isEstadoValidacion();
    }
}