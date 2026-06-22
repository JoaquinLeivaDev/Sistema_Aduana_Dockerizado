package cl.duocuc.aduana_api.repository;

import cl.duocuc.aduana_api.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
    Turno findByPasajeroIdPasajero(Long idPasajero);
    boolean existsByPasajeroIdPasajero(Long idPasajero);
}