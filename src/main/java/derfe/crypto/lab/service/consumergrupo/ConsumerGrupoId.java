package derfe.crypto.lab.service.consumergrupo;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConsumerGrupoId implements Serializable {

    private Long consumerId;

    private Long grupoId;

    public ConsumerGrupoId() {
    }

    public ConsumerGrupoId(Long consumerId, Long grupoId) {
        this.consumerId = consumerId;
        this.grupoId = grupoId;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ConsumerGrupoId)) {
            return false;
        }

        ConsumerGrupoId that = (ConsumerGrupoId) o;

        return Objects.equals(consumerId, that.consumerId)
                && Objects.equals(grupoId, that.grupoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consumerId, grupoId);
    }
}