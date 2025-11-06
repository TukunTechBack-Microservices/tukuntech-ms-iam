package com.upc.tukuntechmsiam.iam.infrastructure.seeding;

import com.upc.tukuntechmsiam.iam.domain.entity.PermissionEntity;
import com.upc.tukuntechmsiam.iam.domain.entity.RoleEntity;
import com.upc.tukuntechmsiam.iam.domain.entity.UserIdentity;
import com.upc.tukuntechmsiam.iam.domain.repositories.PermissionRepository;
import com.upc.tukuntechmsiam.iam.domain.repositories.RoleRepository;
import com.upc.tukuntechmsiam.iam.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@Profile("dev")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.seed.enabled", havingValue = "true", matchIfMissing = false)
public class DevSeed implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DevSeed.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder encoder;

    // ======= Variables externas (con defaults seguros para dev) =======
    @Value("${seed.admin.email:admin@tukuntech.com}")
    private String adminEmail;
    @Value("${seed.admin.password:Admin123}")
    private String adminPassword;

    @Value("${seed.attendant.email:attendant@tukuntech.com}")
    private String attendantEmail;
    @Value("${seed.attendant.password:Attendant123}")
    private String attendantPassword;

    @Value("${seed.patient.email:patient@tukuntech.com}")
    private String patientEmail;
    @Value("${seed.patient.password:Patient123}")
    private String patientPassword;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("🌱 Running IAM DevSeed: initializing default roles, permissions and users...");

        // --- Roles (idempotente) ---
        var admin = roleRepository.findByName("ADMINISTRATOR").orElseGet(() -> {
            var r = new RoleEntity();
            r.setName("ADMINISTRATOR");
            return roleRepository.save(r);
        });

        var attendant = roleRepository.findByName("ATTENDANT").orElseGet(() -> {
            var r = new RoleEntity();
            r.setName("ATTENDANT");
            return roleRepository.save(r);
        });

        var patient = roleRepository.findByName("PATIENT").orElseGet(() -> {
            var r = new RoleEntity();
            r.setName("PATIENT");
            return roleRepository.save(r);
        });

        // --- Permissions (idempotente) ---
        var pRead = permissionRepository.findByName("PATIENT_READ")
                .orElseGet(() -> permissionRepository.save(newPerm("PATIENT_READ")));

        var pWrite = permissionRepository.findByName("PATIENT_WRITE")
                .orElseGet(() -> permissionRepository.save(newPerm("PATIENT_WRITE")));

        // asignaciones (evitar duplicados por Set)
        admin.getPermissions().addAll(Set.of(pRead, pWrite));
        attendant.getPermissions().add(pRead);

        roleRepository.save(admin);
        roleRepository.save(attendant);
        roleRepository.save(patient);

        // --- Users por defecto (idempotente, sin loggear password) ---
        seedUserIfMissing(adminEmail, adminPassword, admin, "ADMINISTRATOR");
        seedUserIfMissing(attendantEmail, attendantPassword, attendant, "ATTENDANT");
        seedUserIfMissing(patientEmail, patientPassword, patient, "PATIENT");

        log.info("✅ IAM seeding completed successfully.");
    }

    private void seedUserIfMissing(String email, String rawPassword, RoleEntity role, String roleName) {
        userRepository.findByEmail(email).orElseGet(() -> {
            var u = new UserIdentity();
            u.setEmail(email);
            u.setPassword(encoder.encode(rawPassword));
            u.setEnabled(true);
            u.getRoles().add(role);
            var saved = userRepository.save(u);
            log.info("👤 User ensured: {} (role: {})", email, roleName);
            return saved;
        });
    }

    private static PermissionEntity newPerm(String name) {
        var perm = new PermissionEntity();
        perm.setName(name);
        return perm;
    }
}
