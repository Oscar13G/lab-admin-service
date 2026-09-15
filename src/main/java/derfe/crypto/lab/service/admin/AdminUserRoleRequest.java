package derfe.crypto.lab.service.admin;
import jakarta.validation.constraints.NotNull;

// Datos necesarios para modificar el rol de un usuario administrativo.
public class AdminUserRoleRequest {

    @NotNull
    private AdminRole role;

    public AdminRole getRole() {
        return role;
    }
}