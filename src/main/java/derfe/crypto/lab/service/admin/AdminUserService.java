package derfe.crypto.lab.service.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Contiene la lógica relacionada con usuarios administrativos.
@Service
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring inyecta automáticamente el repositorio.
    public AdminUserService(
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder) {

        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Busca un usuario administrativo por su username.
    public Optional<AdminUser> findByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    // Guarda un usuario administrativo en PostgreSQL.
    public AdminUser save(AdminUser adminUser) {
        return adminUserRepository.save(adminUser);
    }

    // Lista los usuarios administrativos sin exponer información sensible.
    public List<AdminUserResponse> findAll() {

        return adminUserRepository.findAll()
                .stream()
                .map(adminUser -> new AdminUserResponse(
                        adminUser.getId(),
                        adminUser.getUsername(),
                        adminUser.getRole(),
                        adminUser.isEnabled()
                ))
                .toList();
    }

    // Crea un nuevo usuario administrativo.
    public AdminUserResponse create(AdminUserCreateRequest request) {

        // Evita usernames duplicados.
        if (adminUserRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El username ya existe");
        }

        // La contraseña se transforma a BCrypt antes de almacenarla.
        String passwordHash = passwordEncoder.encode(request.getPassword());

        AdminUser adminUser = new AdminUser(
                request.getUsername(),
                passwordHash,
                request.getRole()
        );

        AdminUser savedUser = adminUserRepository.save(adminUser);

        return new AdminUserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole(),
                savedUser.isEnabled()
        );
    }

    // Busca un usuario administrativo por su ID.
    public Optional<AdminUserResponse> findById(Long id) {

        return adminUserRepository.findById(id)
                .map(adminUser -> new AdminUserResponse(
                        adminUser.getId(),
                        adminUser.getUsername(),
                        adminUser.getRole(),
                        adminUser.isEnabled()
                ));
    }

    // Habilita o deshabilita una cuenta administrativa.
    public Optional<AdminUserResponse> updateStatus(Long id, boolean enabled) {

        return adminUserRepository.findById(id)
                .map(adminUser -> {

                    adminUser.setEnabled(enabled);

                    AdminUser savedUser = adminUserRepository.save(adminUser);

                    return new AdminUserResponse(
                            savedUser.getId(),
                            savedUser.getUsername(),
                            savedUser.getRole(),
                            savedUser.isEnabled()
                    );
                });
    }

    // Modifica el rol de un usuario administrativo.
    public Optional<AdminUserResponse> updateRole(
            Long id,
            AdminRole role) {

        return adminUserRepository.findById(id)
                .map(adminUser -> {

                    adminUser.setRole(role);

                    AdminUser savedUser = adminUserRepository.save(adminUser);

                    return new AdminUserResponse(
                            savedUser.getId(),
                            savedUser.getUsername(),
                            savedUser.getRole(),
                            savedUser.isEnabled()
                    );
                });
    }

    // Modifica la contraseña de un usuario administrativo.
    public Optional<AdminUserResponse> updatePassword(
            Long id,
            String password) {

        return adminUserRepository.findById(id)
                .map(adminUser -> {

                    // La nueva contraseña se convierte a BCrypt antes de almacenarla.
                    String passwordHash = passwordEncoder.encode(password);

                    adminUser.setPasswordHash(passwordHash);

                    AdminUser savedUser = adminUserRepository.save(adminUser);

                    return new AdminUserResponse(
                            savedUser.getId(),
                            savedUser.getUsername(),
                            savedUser.getRole(),
                            savedUser.isEnabled()
                    );
                });
    }

    // Elimina un usuario administrativo por su ID.
    public boolean delete(Long id) {

        if (!adminUserRepository.existsById(id)) {
            return false;
        }

        adminUserRepository.deleteById(id);

        return true;
    }
}