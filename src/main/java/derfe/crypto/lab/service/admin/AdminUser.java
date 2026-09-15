package derfe.crypto.lab.service.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Usuario que puede iniciar sesión en el Admin Service.
@Entity
@Table(name = "admin_users")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre utilizado para iniciar sesión.
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    // La contraseña nunca se guarda en texto plano.
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Permite habilitar o bloquear una cuenta administrativa.
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    // ADMIN o FUNCIONAL.
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private AdminRole role;

    // Constructor requerido por JPA.
    protected AdminUser() {
    }

    public AdminUser(String username, String passwordHash, AdminRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.enabled = true;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public AdminRole getRole() {
        return role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}