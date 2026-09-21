package derfe.crypto.lab.service.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;

import jakarta.validation.Valid;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Endpoints para administrar usuarios del Admin Service.
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private static final Logger auditLogger =
        LoggerFactory.getLogger("AUDIT");

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    // Solo un usuario con rol ADMIN puede consultar las cuentas administrativas.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> findAll() {

        return ResponseEntity.ok(adminUserService.findAll());
    }

    // Consulta un usuario administrativo por su ID.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AdminUserResponse> findById(
            @PathVariable Long id) {

        return adminUserService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Solo ADMIN puede crear nuevas cuentas administrativas.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AdminUserResponse> create(
            @Valid @RequestBody AdminUserCreateRequest request, 
            @AuthenticationPrincipal Jwt jwt) {

        AdminUserResponse createdUser = adminUserService.create(request);

        auditLogger.info(
            "USER_CREATED actor={} id={} username={} role={}",
            jwt.getSubject(),
            createdUser.getId(),
            createdUser.getUsername(),
            createdUser.getRole()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    // Habilita o deshabilita una cuenta administrativa.
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AdminUserResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean enabled,
            @AuthenticationPrincipal Jwt jwt) {
    

        AdminUserResponse updatedUser = adminUserService
            .updateStatus(id, enabled)
            .orElse(null);

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        auditLogger.info(
                "USER_STATUS_CHANGED actor={} id={} username={} enabled={}",
                jwt.getSubject(),
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.isEnabled()
        );

        return ResponseEntity.ok(updatedUser);
    }

    // Modifica el rol de un usuario administrativo.
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<AdminUserResponse> updateRole(
        @PathVariable Long id,
        @Valid @RequestBody AdminUserRoleRequest request,
        @AuthenticationPrincipal Jwt jwt) {

        AdminUserResponse updatedUser = adminUserService
        .updateRole(id, request.getRole())
        .orElse(null);

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        auditLogger.info(
                "USER_ROLE_CHANGED actor={} id={} username={} role={}",
                jwt.getSubject(),
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getRole()
        );

        return ResponseEntity.ok(updatedUser);
    }

    // Modifica la contraseña de un usuario administrativo.
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/password")
    public ResponseEntity<AdminUserResponse> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody AdminUserPasswordRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        AdminUserResponse updatedUser = adminUserService
        .updatePassword(id, request.getPassword())
        .orElse(null);

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        auditLogger.info(
                "USER_PASSWORD_CHANGED actor={} id={} username={}",
                jwt.getSubject(),
                updatedUser.getId(),
                updatedUser.getUsername()
        );

        return ResponseEntity.ok(updatedUser);
    }

    // Elimina un usuario administrativo.
    // Un administrador no puede eliminar su propia cuenta.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        String currentUsername = jwt.getSubject();

        AdminUserResponse userToDelete = adminUserService.findById(id)
                .orElse(null);

        if (userToDelete == null) {
            return ResponseEntity.notFound().build();
        }

        if (userToDelete.getUsername().equals(currentUsername)) {

            auditLogger.warn(
                    "USER_DELETE_DENIED actor={} id={} username={} reason=SELF_DELETE",
                    currentUsername,
                    userToDelete.getId(),
                    userToDelete.getUsername()
            );

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        adminUserService.delete(id);

        auditLogger.info(
            "USER_DELETED actor={} id={} username={}",
            currentUsername,
            userToDelete.getId(),
            userToDelete.getUsername()
        );

        return ResponseEntity.noContent().build();
    }
}