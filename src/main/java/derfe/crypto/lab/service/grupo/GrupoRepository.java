package derfe.crypto.lab.service.grupo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    Optional<Grupo> findByName(String name);
}