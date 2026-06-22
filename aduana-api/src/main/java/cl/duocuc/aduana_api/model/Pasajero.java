package cl.duocuc.aduana_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pasajero")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pasajero {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pasajero_seq")
    @SequenceGenerator(name = "pasajero_seq", sequenceName = "PASAJERO_SEQ", allocationSize = 1)
    @Column(name = "id_pasajero")
    private Long idPasajero;

    @Column(name = "rut", nullable = false, length = 12, unique = true)
    private String rut;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNac;

    @Column(name = "correo", length = 100, unique = true)
    private String correo;

    @OneToMany(mappedBy = "pasajero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Documento> documentos = new ArrayList<>();

    @OneToOne(mappedBy = "pasajero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Turno turno;

    public int calcularEdad() {
        return Period.between(this.fechaNac, LocalDate.now()).getYears();
    }
}