package cl.duocuc.aduana_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "USUARIO_SEQ", allocationSize = 1)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    // Métodos del diagrama de clases
    public boolean login(String username, String passwordHash) {
        return this.username.equals(username) && this.passwordHash.equals(passwordHash);
    }

    public void logout() {
        // En una implementación real invalidaría el token JWT
        // Por ahora documenta el comportamiento esperado
    }
}