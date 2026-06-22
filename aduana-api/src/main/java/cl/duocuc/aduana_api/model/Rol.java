package cl.duocuc.aduana_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_seq")
    @SequenceGenerator(name = "rol_seq", sequenceName = "ROL_SEQ", allocationSize = 1)
    @Column(name = "id_rol")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 30, unique = true)
    private String nombre;
}