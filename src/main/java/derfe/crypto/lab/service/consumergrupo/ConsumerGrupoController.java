package derfe.crypto.lab.service.consumergrupo;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consumer-groups")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
public class ConsumerGrupoController {

    private final ConsumerGrupoService consumerGrupoService;

    public ConsumerGrupoController(
            ConsumerGrupoService consumerGrupoService) {

        this.consumerGrupoService = consumerGrupoService;
    }

    @PostMapping("/{consumerId}/{grupoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsumerGrupo asignarGrupo(
            @PathVariable Long consumerId,
            @PathVariable Long grupoId) {

        return consumerGrupoService.asignarGrupo(
                consumerId,
                grupoId
        );
    }

    @GetMapping
    public List<ConsumerGrupo> findAll() {
        return consumerGrupoService.findAll();
    }

    @DeleteMapping("/{consumerId}/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarAsignacion(
            @PathVariable Long consumerId,
            @PathVariable Long grupoId) {

        consumerGrupoService.eliminarAsignacion(
                consumerId,
                grupoId
        );
    }
}