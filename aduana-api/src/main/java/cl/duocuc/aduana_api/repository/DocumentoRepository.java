package cl.duocuc.aduana_api.repository;

import cl.duocuc.aduana_api.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    // Busca todos los documentos de un pasajero por su id
    List<Documento> findByPasajeroIdPasajero(Long idPasajero);
}