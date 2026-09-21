package derfe.crypto.lab.service.grupo;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/groups")
@PreAuthorize("hasAnyRole('ADMIN', 'FUNCIONAL')")
public class GrupoController {

    private static final Logger auditLogger =
        LoggerFactory.getLogger("AUDIT");

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
    public Grupo createGrupo(
            @RequestBody Grupo grupo,
            @AuthenticationPrincipal Jwt jwt) {

        Grupo createdGrupo = grupoService.createGrupo(
                grupo.getName(),
                grupo.isCriptoServ(),
                grupo.isCertificadoServ()
        );

        auditLogger.info(
                "GROUP_CREATED actor={} id={} name={} criptoServ={} certificadoServ={}",
                jwt.getSubject(),
                createdGrupo.getId(),
                createdGrupo.getName(),
                createdGrupo.isCriptoServ(),
                createdGrupo.isCertificadoServ()
        );

        return createdGrupo;
    }

    @PatchMapping("/{id}/cripto-serv")
    public Grupo updateCriptoServ(
            @PathVariable Long id,
            @RequestParam boolean enabled,
            @AuthenticationPrincipal Jwt jwt) {

        Grupo updatedGrupo = grupoService.updateCriptoServ(id, enabled)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Grupo no encontrado"
                        )
                );

        auditLogger.info(
                "GROUP_CRYPTO_SERVICE_CHANGED actor={} id={} name={} enabled={}",
                jwt.getSubject(),
                updatedGrupo.getId(),
                updatedGrupo.getName(),
                updatedGrupo.isCriptoServ()
        );

        return updatedGrupo;
    }

    @PatchMapping("/{id}/certificado-serv")
    public Grupo updateCertificadoServ(
            @PathVariable Long id,
            @RequestParam boolean enabled,
            @AuthenticationPrincipal Jwt jwt) {

        Grupo updatedGrupo = grupoService.updateCertificadoServ(id, enabled)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Grupo no encontrado"
                        )
                );

        auditLogger.info(
                "GROUP_CERTIFICATE_SERVICE_CHANGED actor={} id={} name={} enabled={}",
                jwt.getSubject(),
                updatedGrupo.getId(),
                updatedGrupo.getName(),
                updatedGrupo.isCertificadoServ()
        );

        return updatedGrupo;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGrupo(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        Grupo grupo = grupoService.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Grupo no encontrado"
                        )
                );

        grupoService.deleteGrupo(id);

        auditLogger.info(
                "GROUP_DELETED actor={} id={} name={}",
                jwt.getSubject(),
                grupo.getId(),
                grupo.getName()
        );
    }
}