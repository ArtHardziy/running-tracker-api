package de.bergmann.runnertracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.bergmann.runnertracker.model.*;
import de.bergmann.runnertracker.model.dto.RunnerUserDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class TestUtils {

    public static RunnerUser setUpTestRunnerWithRun() {
        final Role testRole = new Role();
        testRole.setRoleType(RoleType.USER);
        RunnerUser testRunner = RunnerUser.builder()
                .id(1L)
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .birthDate(LocalDate.of(2002, 01, 01))
                .username("test")
                .email("test@test.com")
                .password("test")
                .role(testRole)
                .createdAt(LocalDateTime.of(LocalDate.of(2023, 12, 03), LocalTime.of(12, 12)))
                .updatedAt(LocalDateTime.of(LocalDate.of(2023, 12, 03), LocalTime.of(12, 12)))
                .sex(Sex.THEY)
                .build();
        Run testRun = Run.builder()
                .id(1L)
                .startLatitude(27.546011)
                .startLongitude(53.912156)
                .finishLongitude(53.904709)
                .finishLatitude(27.554031)
                .startDateTime(LocalDateTime.of(LocalDate.of(2023,12, 03), LocalTime.of(12,12)))
                .finishDateTime(LocalDateTime.of(LocalDate.of(2023,12, 03), LocalTime.of(12,12)).plusSeconds(370))
                .runningUser(new RunnerUser())
                .runningUserId(1)
                .runningUser(testRunner)
                .build();
        List<Run> runs = new ArrayList<>();
        runs.add(testRun);
        testRunner.setRuns(runs);
        return testRunner;
    }

    public static RunnerUserDTO convertToDto(RunnerUser user) {
        return RunnerUserDTO.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .username(user.getUsername())
                .email(user.getEmail())
                .birthdate(user.getBirthDate())
                .sex(user.getSex())
                .build();
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
