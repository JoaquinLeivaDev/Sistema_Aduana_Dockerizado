package cl.duocuc.aduana_api.repository;

import cl.duocuc.aduana_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    boolean existsByUsername(String username);
}