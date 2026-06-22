package cl.duocuc.aduana_api.repository;

import cl.duocuc.aduana_api.model.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {

    // Busca pasajero por RUT — fundamental para el control fronterizo
    Pasajero findByRut(String rut);

    // Verifica si ya existe un RUT — usado en registrarPasajero para evitar duplicados
    boolean existsByRut(String rut);
}