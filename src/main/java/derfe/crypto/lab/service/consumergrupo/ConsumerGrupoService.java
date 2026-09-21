package derfe.crypto.lab.service.consumergrupo;

import derfe.crypto.lab.service.consumer.Consumer;
import derfe.crypto.lab.service.consumer.ConsumerRepository;
import derfe.crypto.lab.service.grupo.Grupo;
import derfe.crypto.lab.service.grupo.GrupoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerGrupoService {

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

        Consumer consumer = consumerRepository.findById(consumerId)
                .orElseThrow(() ->
                        new RuntimeException("Consumidor no encontrado")
                );

        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() ->
                        new RuntimeException("Grupo no encontrado")
                );

        ConsumerGrupoId id = new ConsumerGrupoId(
                consumerId,
                grupoId
        );

        if (consumerGrupoRepository.existsById(id)) {
            throw new RuntimeException(
                    "El consumidor ya pertenece a este grupo"
            );
        }

        ConsumerGrupo consumerGrupo = new ConsumerGrupo(
                consumer,
                grupo
        );

        return consumerGrupoRepository.save(consumerGrupo);
    }

    public List<ConsumerGrupo> findAll() {
        return consumerGrupoRepository.findAll();
    }

    public void eliminarAsignacion(Long consumerId, Long grupoId) {

        ConsumerGrupoId id = new ConsumerGrupoId(
                consumerId,
                grupoId
        );

        if (!consumerGrupoRepository.existsById(id)) {
            throw new RuntimeException(
                    "La asignación no existe"
            );
        }

        consumerGrupoRepository.deleteById(id);
    }
}