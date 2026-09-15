package derfe.crypto.lab.service.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Crea el usuario ADMIN inicial si todavía no existe.
@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final AdminUserService adminUserService;
    private final PasswordEncoder passwordEncoder;

    private final String username;
    private final String password;

    public AdminUserInitializer(
            AdminUserService adminUserService,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_USERNAME}") String username,
            @Value("${ADMIN_PASSWORD}") String password) {

        this.adminUserService = adminUserService;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(String... args) {

        // Evita crear el mismo usuario más de una vez.
        if (adminUserService.findByUsername(username).isPresent()) {
            return;
        }

        String passwordHash = passwordEncoder.encode(password);

        AdminUser adminUser = new AdminUser(
                username,
                passwordHash,
                AdminRole.ADMIN
        );

        adminUserService.save(adminUser);
    }
}