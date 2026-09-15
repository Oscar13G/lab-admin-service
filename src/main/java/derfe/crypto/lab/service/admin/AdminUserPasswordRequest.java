package derfe.crypto.lab.service.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Datos necesarios para modificar la contraseña.
public class AdminUserPasswordRequest {

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    public String getPassword() {
        return password;
    }
}