package derfe.crypto.lab.service.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

// Expone operaciones HTTP relacionadas con los consumidores.
@RestController
@RequestMapping("/consumers")
public class ConsumerController {

    private static final Logger auditLogger =
        LoggerFactory.getLogger("AUDIT");

    private final ConsumerService consumerService;

    // Spring inyecta automáticamente el servicio.
    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    // Crea un nuevo consumidor a partir de los datos recibidos.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consumer createConsumer(
            @RequestBody Consumer consumer,
            @AuthenticationPrincipal Jwt jwt) {

        Consumer createdConsumer =
                consumerService.createConsumer(consumer.getName());

        auditLogger.info(
                "CONSUMER_CREATED actor={} id={} name={}",
                jwt.getSubject(),
                createdConsumer.getId(),
                createdConsumer.getName()
        );

        return createdConsumer;
    }

    // Devuelve todos los consumidores registrados.
    @GetMapping
    public List<Consumer> findAll() {
        return consumerService.findAll();
    }

    // Busca un consumidor por su nombre.
    // Si no existe, devuelve HTTP 404.
    @GetMapping("/{name}")
    public Consumer findByName(@PathVariable String name) {
        return consumerService.findByName(name)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Consumer no encontrado"
                        )
                );
    }

    // Habilita o deshabilita un consumidor existente.
    @PatchMapping("/{id}/status")
    public Consumer updateStatus(
            @PathVariable Long id,
            @RequestParam boolean enabled,
            @AuthenticationPrincipal Jwt jwt) {

        Consumer updatedConsumer = consumerService.updateStatus(id, enabled)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Consumer no encontrado"
                        )
                );

        auditLogger.info(
                "CONSUMER_STATUS_CHANGED actor={} id={} name={} enabled={}",
                jwt.getSubject(),
                updatedConsumer.getId(),
                updatedConsumer.getName(),
                updatedConsumer.isEnabled()
        );

        return updatedConsumer;
    }

    // Elimina un consumidor existente.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConsumer(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        Consumer consumer = consumerService.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Consumer no encontrado"
                        )
                );

        consumerService.deleteConsumer(id);

        auditLogger.info(
                "CONSUMER_DELETED actor={} id={} name={}",
                jwt.getSubject(),
                consumer.getId(),
                consumer.getName()
        );
    }
}

