package de.bergmann.runnertracker.config;

import de.bergmann.runnertracker.model.Role;
import de.bergmann.runnertracker.model.RoleType;
import de.bergmann.runnertracker.model.RunnerUser;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@AllArgsConstructor
public class SeedDataConfig implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final RunningTrackerUserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (userService.countAllUsers() == 0) {
            var adminRole = new Role();
            adminRole.setRoleType(RoleType.ADMIN);
            var dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            var admin = RunnerUser.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .username("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("admin"))
                    .role(adminRole)
                    .birthDate(LocalDate.parse("2002-05-07", dateFormat))
                    .build();
            userService.save(admin);
            log.debug("Created ADMIN user - [{}]", admin);
        }
    }
}
