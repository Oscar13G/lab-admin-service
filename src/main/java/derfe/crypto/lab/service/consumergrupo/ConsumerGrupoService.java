package derfe.crypto.lab.service.consumergrupo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import derfe.crypto.lab.service.consumer.Consumer;
import derfe.crypto.lab.service.consumer.ConsumerRepository;
import derfe.crypto.lab.service.grupo.Grupo;
import derfe.crypto.lab.service.grupo.GrupoRepository;

@Service
public class ConsumerGrupoService {
    private static final Logger log =
            LoggerFactory.getLogger(ConsumerGrupoService.class);

    private static final Logger audit =
            LoggerFactory.getLogger("AUDIT");

    private final ConsumerGrupoRepository consumerGrupoRepository;
    private final ConsumerRepository consumerRepository;
    private final GrupoRepository grupoRepository;

    public ConsumerGrupoService(
            ConsumerGrupoRepository consumerGrupoRepository,
            ConsumerRepository consumerRepository,
            GrupoRepository grupoRepository) {

        this.consumerGrupoRepository = consumerGrupoRepository;
        this.consumerRepository = consumerRepository;
        this.grupoRepository = grupoRepository;
    }

    
    public ConsumerGrupo asignarGrupo(Long consumerId, Long grupoId) {

        log.info("Iniciando asignación de grupo. consumerId={}, grupoId={}",
                consumerId, grupoId);

        Consumer consumer = consumerRepository.findById(consumerId)
            .orElseThrow(() -> {
                log.warn("Consumidor no encontrado. consumerId={}", consumerId);
                return new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Consumidor no encontrado");
            });

        Grupo grupo = grupoRepository.findById(grupoId)
            .orElseThrow(() -> {
                log.warn("Grupo no encontrado. grupoId={}", grupoId);
                return new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Grupo no encontrado");
            });

        ConsumerGrupoId id = new ConsumerGrupoId(
                consumerId,
                grupoId
        );

        if (consumerGrupoRepository.existsById(id)) {
            log.warn("Asignación duplicada. consumerId={}, grupoId={}",
                    consumerId, grupoId);

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El consumidor ya pertenece a este grupo"
            );
        }

        ConsumerGrupo consumerGrupo = new ConsumerGrupo(
                consumer,
                grupo
        );

        ConsumerGrupo resultado = consumerGrupoRepository.save(consumerGrupo);

        log.info("Grupo asignado correctamente. consumerId={}, grupoId={}",
                consumerId, grupoId);

        audit.info("Asignación de grupo realizada. consumerId={}, grupoId={}",
                consumerId, grupoId);

        return resultado;
    }

    public List<ConsumerGrupo> findAll() {
        log.info("Consultando todas las asignaciones de consumidores y grupos");

        List<ConsumerGrupo> asignaciones = consumerGrupoRepository.findAll();

        log.info("Consulta de asignaciones completada. total={}",
                asignaciones.size());

        return asignaciones;
    }

    
    public void eliminarAsignacion(Long consumerId, Long grupoId) {

        log.info("Iniciando eliminación de asignación. consumerId={}, grupoId={}",
                consumerId, grupoId);

        ConsumerGrupoId id = new ConsumerGrupoId(
                consumerId,
                grupoId
        );

        if (!consumerGrupoRepository.existsById(id)) {
            log.warn("No se puede eliminar la asignación: no existe. consumerId={}, grupoId={}",
                    consumerId, grupoId);

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "La asignación no existe"
            );
        }

        consumerGrupoRepository.deleteById(id);

        log.info("Asignación eliminada correctamente. consumerId={}, grupoId={}",
                consumerId, grupoId);

        audit.info("Eliminación de asignación realizada. consumerId={}, grupoId={}",
                consumerId, grupoId);
    }

}
