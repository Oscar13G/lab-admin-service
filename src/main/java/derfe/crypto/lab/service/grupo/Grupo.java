package derfe.crypto.lab.service.grupo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "grupos")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "cripto_serv", nullable = false)
    private boolean criptoServ = false;

    @Column(name = "certificado_serv", nullable = false)
    private boolean certificadoServ = false;

    protected Grupo() {
    }

    public Grupo(String name, boolean criptoServ, boolean certificadoServ) {
        this.name = name;
        this.criptoServ = criptoServ;
        this.certificadoServ = certificadoServ;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCriptoServ() {
        return criptoServ;
    }

    public void setCriptoServ(boolean criptoServ) {
        this.criptoServ = criptoServ;
    }

    public boolean isCertificadoServ() {
        return certificadoServ;
    }

    public void setCertificadoServ(boolean certificadoServ) {
        this.certificadoServ = certificadoServ;
    }
}