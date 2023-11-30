package de.bergmann.runnertracker.config;

import de.bergmann.runnertracker.model.Role;
import de.bergmann.runnertracker.model.RoleType;
import de.bergmann.runnertracker.model.RunningTrackerUser;
import de.bergmann.runnertracker.service.RunningTrackerUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

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
            var dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            var admin = RunningTrackerUser.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .username("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("admin"))
                    .role(adminRole)
                    .birthDate(LocalDate.parse("07-05-2002", dateFormat))
                    .build();
            userService.save(admin);
            log.debug("Created ADMIN user - [{}]", admin);
        }
    }
}
