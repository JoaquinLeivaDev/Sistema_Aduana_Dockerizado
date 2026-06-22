package cl.duocuc.aduana_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reporte")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reporte_seq")
    @SequenceGenerator(name = "reporte_seq", sequenceName = "REPORTE_SEQ", allocationSize = 1)
    @Column(name = "id_reporte")
    private Long idReporte;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Lob
    @Column(name = "datos", columnDefinition = "CLOB")
    private String datos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}