package derfe.crypto.lab.service.consumergrupo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerGrupoRepository
        extends JpaRepository<ConsumerGrupo, ConsumerGrupoId> {
}