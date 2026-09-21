package derfe.crypto.lab.service.consumergrupo;

import derfe.crypto.lab.service.consumer.Consumer;
import derfe.crypto.lab.service.grupo.Grupo;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "consumer_grupo")
public class ConsumerGrupo {

    @EmbeddedId
    private ConsumerGrupoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("consumerId")
    @JoinColumn(name = "consumer_id", nullable = false)
    private Consumer consumer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("grupoId")
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    protected ConsumerGrupo() {
    }

    public ConsumerGrupo(Consumer consumer, Grupo grupo) {
        this.consumer = consumer;
        this.grupo = grupo;
        this.id = new ConsumerGrupoId(
                consumer.getId(),
                grupo.getId()
        );
    }

    public ConsumerGrupoId getId() {
        return id;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Grupo getGrupo() {
        return grupo;
    }
}
