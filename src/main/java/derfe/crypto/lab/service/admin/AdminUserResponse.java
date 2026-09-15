package derfe.crypto.lab.service.admin;

// Datos públicos de un usuario administrativo.
// Nunca expone el hash de la contraseña.
public class AdminUserResponse {

    private final Long id;
    private final String username;
    private final AdminRole role;
    private final boolean enabled;

    public AdminUserResponse(
            Long id,
            String username,
            AdminRole role,
            boolean enabled) {

        this.id = id;
        this.username = username;
        this.role = role;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public AdminRole getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }
}