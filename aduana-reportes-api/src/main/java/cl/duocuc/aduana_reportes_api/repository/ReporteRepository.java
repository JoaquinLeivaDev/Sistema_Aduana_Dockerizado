package cl.duocuc.aduana_reportes_api.repository;

import cl.duocuc.aduana_reportes_api.model.Reporte;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ReporteRepository {
    Optional<Reporte> findById(Long id);
}
