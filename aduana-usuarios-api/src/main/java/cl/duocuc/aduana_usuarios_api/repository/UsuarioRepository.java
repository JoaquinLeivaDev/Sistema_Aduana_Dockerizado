package cl.duocuc.aduana_usuarios_api.repository;

import cl.duocuc.aduana_usuarios_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    boolean existsByUsername(String username);
}
