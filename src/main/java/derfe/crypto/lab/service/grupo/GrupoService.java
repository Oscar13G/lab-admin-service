package derfe.crypto.lab.service.grupo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public List<Grupo> findAll() {
        return grupoRepository.findAll();
    }

    public Optional<Grupo> findById(Long id) {
        return grupoRepository.findById(id);
    }

    public Optional<Grupo> findByName(String name) {
        return grupoRepository.findByName(name);
    }

    public Grupo createGrupo(String name, boolean criptoServ, boolean certificadoServ) {
        Grupo grupo = new Grupo(name, criptoServ, certificadoServ);
        return grupoRepository.save(grupo);
    }

    public Optional<Grupo> updateGrupo(
            Long id,
            String name,
            boolean criptoServ,
            boolean certificadoServ) {

        Optional<Grupo> optionalGrupo = grupoRepository.findById(id);

        if (optionalGrupo.isEmpty()) {
            return Optional.empty();
        }

        Grupo grupo = optionalGrupo.get();

        grupo.setName(name);
        grupo.setCriptoServ(criptoServ);
        grupo.setCertificadoServ(certificadoServ);

        return Optional.of(grupoRepository.save(grupo));
    }

    public boolean deleteGrupo(Long id) {
        if (!grupoRepository.existsById(id)) {
            return false;
        }

        grupoRepository.deleteById(id);
        return true;
    }
}