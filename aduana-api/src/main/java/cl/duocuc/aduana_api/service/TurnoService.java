package cl.duocuc.aduana_api.service;

import cl.duocuc.aduana_api.dto.TurnoRequestDTO;
import cl.duocuc.aduana_api.dto.TurnoResponseDTO;
import cl.duocuc.aduana_api.exception.PasajeroNotFoundException;
import cl.duocuc.aduana_api.exception.TurnoNotFoundException;
import cl.duocuc.aduana_api.model.Pasajero;
import cl.duocuc.aduana_api.model.Turno;
import cl.duocuc.aduana_api.repository.PasajeroRepository;
import cl.duocuc.aduana_api.repository.TurnoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TurnoService {

    private static final Logger log = LoggerFactory.getLogger(TurnoService.class);

    private final TurnoRepository turnoRepository;
    private final PasajeroRepository pasajeroRepository;

    public TurnoService(TurnoRepository turnoRepository,
                        PasajeroRepository pasajeroRepository) {
        this.turnoRepository = turnoRepository;
        this.pasajeroRepository = pasajeroRepository;
    }

    public List<TurnoResponseDTO> obtenerTodos() {
        log.info("Obteniendo lista de todos los turnos");
        return turnoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TurnoResponseDTO buscarPorId(Long id) {
        log.info("Buscando turno con id: {}", id);
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Turno con id {} no encontrado", id);
                    return new TurnoNotFoundException("Turno con id " + id + " no encontrado");
                });
        return toResponseDTO(turno);
    }

    public TurnoResponseDTO buscarPorPasajero(Long idPasajero) {
        log.info("Buscando turno del pasajero con id: {}", idPasajero);
        Turno turno = turnoRepository.findByPasajeroIdPasajero(idPasajero);
        if (turno == null) {
            log.error("No existe turno para el pasajero con id: {}", idPasajero);
            throw new TurnoNotFoundException("No existe turno para el pasajero con id " + idPasajero);
        }
        return toResponseDTO(turno);
    }

    public TurnoResponseDTO registrarTurno(TurnoRequestDTO dto) {
        log.info("Registrando turno para pasajero id: {}", dto.getIdPasajero());

        Pasajero pasajero = pasajeroRepository.findById(dto.getIdPasajero())
                .orElseThrow(() -> new PasajeroNotFoundException(
                        "Pasajero con id " + dto.getIdPasajero() + " no encontrado"));

        if (turnoRepository.existsByPasajeroIdPasajero(dto.getIdPasajero())) {
            log.error("El pasajero con id {} ya tiene un turno asignado", dto.getIdPasajero());
            throw new IllegalArgumentException("El pasajero ya tiene un turno asignado");
        }

        Turno turno = new Turno();
        turno.setNumero(dto.getNumero());
        turno.setEstado(dto.getEstado());
        turno.setPasajero(pasajero);

        Turno guardado = turnoRepository.save(turno);
        log.info("Turno registrado con id: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    // Regla de negocio clave: asignar ventanilla
    public TurnoResponseDTO asignarVentanilla(Long id, Integer numeroVentanilla) {
        log.info("Asignando ventanilla {} al turno con id: {}", numeroVentanilla, id);
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException(
                        "Turno con id " + id + " no encontrado"));

        turno.asignarVentanilla(numeroVentanilla);

        Turno actualizado = turnoRepository.save(turno);
        log.info("Ventanilla {} asignada al turno {}", numeroVentanilla, id);
        return toResponseDTO(actualizado);
    }

    public TurnoResponseDTO actualizarEstado(Long id, String nuevoEstado) {
        log.info("Actualizando estado del turno con id: {} a {}", id, nuevoEstado);
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException(
                        "Turno con id " + id + " no encontrado"));

        turno.setEstado(nuevoEstado);
        Turno actualizado = turnoRepository.save(turno);
        log.info("Estado del turno {} actualizado a {}", id, nuevoEstado);
        return toResponseDTO(actualizado);
    }

    public void eliminarTurno(Long id) {
        log.info("Eliminando turno con id: {}", id);
        if (!turnoRepository.existsById(id)) {
            log.error("No se puede eliminar: turno con id {} no existe", id);
            throw new TurnoNotFoundException("Turno con id " + id + " no encontrado");
        }
        turnoRepository.deleteById(id);
        log.info("Turno con id {} eliminado exitosamente", id);
    }

    private TurnoResponseDTO toResponseDTO(Turno t) {
        return new TurnoResponseDTO(
                t.getId(),
                t.getNumero(),
                t.getEstado(),
                t.getPasajero().getIdPasajero(),
                t.getPasajero().getNombre()
        );
    }
}