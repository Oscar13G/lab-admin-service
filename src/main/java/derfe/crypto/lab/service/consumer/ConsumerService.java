package derfe.crypto.lab.service.consumer;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

// Contiene la lógica de negocio relacionada con los consumidores.
// Esta capa utiliza el repositorio para acceder a PostgreSQL.
@Service
public class ConsumerService {

    private final ConsumerRepository consumerRepository;

    // Spring inyecta automáticamente el repositorio mediante el constructor.
    public ConsumerService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    // Busca un consumidor utilizando su nombre único.
    public Optional<Consumer> findByName(String name) {
        return consumerRepository.findByName(name);
    }

    // Busca un consumidor por su identificador.
    public Optional<Consumer> findById(Long id) {
        return consumerRepository.findById(id);
    }

    // Obtiene todos los consumidores registrados.
    public List<Consumer> findAll() {
        return consumerRepository.findAll();
    }

    // Crea y guarda un nuevo consumidor.
    public Consumer createConsumer(String name) {
        Consumer consumer = new Consumer(name);
        return consumerRepository.save(consumer);
    }

    // Actualiza el estado habilitado/deshabilitado de un consumidor.
    public Optional<Consumer> updateStatus(Long id, boolean enabled) {
        Optional<Consumer> optionalConsumer = consumerRepository.findById(id);
        if (optionalConsumer.isEmpty()) {
            return Optional.empty();
        }
        Consumer consumer = optionalConsumer.get();
        consumer.setEnabled(enabled);
        Consumer updatedConsumer = consumerRepository.save(consumer);
        return Optional.of(updatedConsumer);
    }

    // Elimina un consumidor por su identificador.
    public boolean deleteConsumer(Long id) {
        if (!consumerRepository.existsById(id)) {
            return false;
        }
        consumerRepository.deleteById(id);
        return true;
    }
}