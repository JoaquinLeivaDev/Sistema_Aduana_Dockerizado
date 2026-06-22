package cl.duocuc.aduana_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documento_seq")
    @SequenceGenerator(name = "documento_seq", sequenceName = "DOCUMENTO_SEQ", allocationSize = 1)
    private Long id;
    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "estado_validacion", nullable = false)
    private boolean estadoValidacion = false;

    @Column(name = "url_archivo", nullable = false)
    private String urlArchivo;

    @ManyToOne
    @JoinColumn(name = "id_pasajero")
    private Pasajero pasajero;
}