package cl.duocuc.aduana_api.repository;

import cl.duocuc.aduana_api.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByUsuario_Id(Long id);
    List<Reporte> findByTipo(String tipo);
}