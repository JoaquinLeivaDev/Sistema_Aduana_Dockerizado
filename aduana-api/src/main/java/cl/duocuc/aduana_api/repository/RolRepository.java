package cl.duocuc.aduana_api.repository;

import cl.duocuc.aduana_api.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}