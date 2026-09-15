package derfe.crypto.lab.service.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Datos necesarios para crear un usuario administrativo.
public class AdminUserCreateRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotNull
    private AdminRole role;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AdminRole getRole() {
        return role;
    }
}