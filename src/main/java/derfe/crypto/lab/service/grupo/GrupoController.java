package derfe.crypto.lab.service.grupo;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/groups")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public List<Grupo> findAll() {
        return grupoService.findAll();
    }

    @GetMapping("/{id}")
    public Grupo findById(@PathVariable Long id) {
        return grupoService.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Grupo no encontrado"
                        )
                );
    }

    @GetMapping("/name/{name}")
    public Grupo findByName(@PathVariable String name) {
        return grupoService.findByName(name)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Grupo no encontrado"
                        )
                );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Grupo createGrupo(@RequestBody Grupo grupo) {
        return grupoService.createGrupo(
                grupo.getName(),
                grupo.isCriptoServ(),
                grupo.isCertificadoServ()
        );
    }

   @PatchMapping("/{id}/cripto-serv")
public Grupo updateCriptoServ(
        @PathVariable Long id,
        @RequestParam boolean enabled) {

    return grupoService.updateCriptoServ(id, enabled)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Grupo no encontrado"
                    )
            );
        }
@PatchMapping("/{id}/certificado-serv")
public Grupo updateCertificadoServ(
        @PathVariable Long id,
        @RequestParam boolean enabled) {

    return grupoService.updateCertificadoServ(id, enabled)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Grupo no encontrado"
                    )
            );
        }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGrupo(@PathVariable Long id) {

        boolean deleted = grupoService.deleteGrupo(id);

        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Grupo no encontrado"
            );
        }
    }
}